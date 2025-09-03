package cn.labzen.tool.feature.id;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;

/**
 * Snowflake 64 位 ID：
 * <p>
 * 基于Twitter的Snowflake算法实现分布式高效有序ID生产黑科技(sequence)。 <br/>
 * SnowFlake的结构如下(每部分用-分开):<br/>
 * bit 分配（与常见实现兼容）：<br/>
 * 1 符号位 | 41 时间戳（ms，自定义 epoch 起） | 5 数据中心 | 5 机器 | 12 序列<br/>
 * <b>0-0000000000 0000000000 0000000000 0000000000 0-00000-00000-000000000000</b>
 * <li>1. 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * <li>2. 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截) 得到的值， 这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序 BASELINE 常量）。
 * <li>3. 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
 * <li>4. 10位的数据机器位，可以部署在1024个节点，包括5位dataCenterId和5位workerId
 * <li>5. 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 * <p><br/>
 * 加起来刚好64位，为一个Long型。<br/>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * <hr/>
 * 主要特性：
 * <li>1) 高并发：使用 1ms 缓存时钟，减少系统调用；同步块极小。
 * <li>2) 时钟回退策略：可配置（WAIT/FAST_FAIL/AUTO），默认 AUTO：<br/>
 * 轻微回退（<= maxRollbackMs）：自旋+短暂休眠等待恢复；<br/>
 * 超过阈值则抛出异常（可改为强等候）。
 * <li>3) 序列溢出：与经典实现一致，阻塞到下一毫秒（可微自旋，降低 Thread.sleep 抖动）。
 * <li>4) 安全性：参数校验；线程安全；可随机化首个序列，减少热点偶数问题。
 * <li>5) 可配置：epoch、位宽、回退阈值、等待策略等。
 */
public final class Snowflake {

  private final long epoch;

  private final long sequenceMask;

  private final int timestampShift;

  private final long datacenterId; // 已左移
  private final long workerId;     // 已左移

  // 策略
  private final ClockRollbackPolicy rollbackPolicy;
  private final long maxRollbackMs;
  private final boolean randomizeFirstSequence;

  // 运行态
  private long lastTs = -1L;
  private long sequence = 0L;

  Snowflake(SnowflakeBuilder builder) {
    this.epoch = builder.epoch;
    int workerBits = builder.workerBits;
    int datacenterBits = builder.datacenterBits;
    int sequenceBits = builder.sequenceBits;

    // 范围与掩码
    long maxWorkerId = ~(-1L << workerBits);
    long maxDatacenterId = ~(-1L << datacenterBits);
    this.sequenceMask = ~(-1L << sequenceBits);

    checkRange("workerId", builder.workerId, 0, maxWorkerId);
    checkRange("datacenterId", builder.datacenterId, 0, maxDatacenterId);

    // 位移
    int datacenterShift = sequenceBits + workerBits;
    this.timestampShift = sequenceBits + workerBits + datacenterBits;

    this.workerId = builder.workerId << sequenceBits;
    this.datacenterId = builder.datacenterId << datacenterShift;

    this.rollbackPolicy = builder.rollbackPolicy;
    this.maxRollbackMs = builder.maxRollbackMs;
    this.randomizeFirstSequence = builder.randomizeFirstSequence;
  }

  @SuppressWarnings("SameParameterValue")
  private void checkRange(String name, long v, long min, long max) {
    if (v < min || v > max) {
      throw new IllegalArgumentException("%s out of range: %d (allowed: %d..%d)".formatted(name, v, min, max));
    }
  }

  // ===================================================================================================================

  /**
   * 线程安全：同步粒度极小。
   */
  public synchronized long nextId() {
    long ts = SystemClock.now();

    // 处理时钟回退
    if (ts < lastTs) {
      ts = handleClockRollback(ts, lastTs);
    }

    if (ts == lastTs) {
      // 同毫秒内自增
      sequence = (sequence + 1) & sequenceMask;
      if (sequence == 0L) {
        // 序列溢出 -> 等到下一毫秒
        ts = waitNextMillis(lastTs);
      }
    } else {
      // 新的毫秒：序列重置（可选随机起点，规避偶数偏置）
      sequence = randomizeFirstSequence ? ThreadLocalRandom.current().nextInt(4) : 0L;
    }

    lastTs = ts;

    // 拼接 64 位 ID
    return ((ts - epoch) << timestampShift) | datacenterId | workerId | sequence;
  }

  /**
   * 回退处理
   */
  private long handleClockRollback(long current, long last) {
    long diff = last - current;

    // FAST_FAIL：直接报错
    if (rollbackPolicy == ClockRollbackPolicy.FAST_FAIL) {
      throw new IllegalStateException("Clock moved backwards by " + diff + " ms");
    }

    // AUTO：轻微回退等待恢复；过阈值 fast-fail
    if (rollbackPolicy == ClockRollbackPolicy.AUTO) {
      if (diff <= maxRollbackMs) {
        // 短暂等待：优先自旋，偶尔纳秒级让步
        @SuppressWarnings("UnnecessaryLocalVariable")
        long deadline = last;
        long t;
        do {
          // 把 CPU 忙等成本压到最小
          LockSupport.parkNanos(1000L); // ~1us 让步
          t = SystemClock.now();
        } while (t < deadline);

        return t;
      }
      throw new IllegalStateException("Clock moved backwards by " + diff + " ms (> " + maxRollbackMs + " ms)");
    }

    // WAIT：无条件等到 last
    long t2;
    do {
      LockSupport.parkNanos(1000L);
      t2 = SystemClock.now();
    } while (t2 < last);
    return t2;
  }

  /**
   * 等到下一毫秒
   */
  private static long waitNextMillis(long lastTs) {
    long ts;
    do {
      // 先尝试自旋 + 纳秒级让步，尽量避免 Thread.sleep 带来的调度抖动
      LockSupport.parkNanos(200_000L); // 0.2ms 级别
      ts = SystemClock.now();
      if (ts == lastTs) {
        // 兜底：当系统时钟更新不积极时，短睡 0ms 触发调度
        Thread.onSpinWait();
      }
    } while (ts <= lastTs);
    return ts;
  }

  public static void main(String[] args) {
    // 创建实例（与 Kotlin 版本默认参数等价：数据中心/工作节点各 0..31）
    Snowflake sf = new SnowflakeBuilder().datacenterId(0)
                                         .workerId(0)
                                         .rollbackPolicy(ClockRollbackPolicy.AUTO)
                                         .maxRollbackMs(5)
                                         .randomizeFirstSequence(true)
                                         .build();

    System.out.println(sf.nextId());
    System.out.println(sf.nextId());
    System.out.println(sf.nextId());
    System.out.println(sf.nextId());
    System.out.println(sf.nextId());
  }
}
