package cn.labzen.tool.feature.id;

import java.util.Objects;

public final class SnowflakeBuilder {

  // ======================== 默认常量 ========================
  public static final long DEFAULT_EPOCH = 1420041600000L;

  public static final int DEFAULT_WORKER_BITS = 5;
  public static final int DEFAULT_DATACENTER_BITS = 5;
  public static final int DEFAULT_SEQUENCE_BITS = 12;

  long epoch = DEFAULT_EPOCH;
  int workerBits = DEFAULT_WORKER_BITS;
  int datacenterBits = DEFAULT_DATACENTER_BITS;
  int sequenceBits = DEFAULT_SEQUENCE_BITS;

  long workerId;
  long datacenterId;

  ClockRollbackPolicy rollbackPolicy = ClockRollbackPolicy.AUTO;
  long maxRollbackMs = 5L;
  boolean randomizeFirstSequence = true;

  public SnowflakeBuilder workerId(long workerId) {
    this.workerId = workerId;
    return this;
  }

  public SnowflakeBuilder datacenterId(long datacenterId) {
    this.datacenterId = datacenterId;
    return this;
  }

  public SnowflakeBuilder epoch(long epochMs) {
    if (epochMs < 0) {
      throw new IllegalArgumentException("epoch must be >= 0");
    }
    this.epoch = epochMs;
    return this;
  }

  /**
   * 仅当你非常清楚 bit 分配影响，请再调整。
   */
  public SnowflakeBuilder bits(int workerBits, int datacenterBits, int sequenceBits) {
    if (workerBits <= 0 || datacenterBits <= 0 || sequenceBits <= 0) {
      throw new IllegalArgumentException("bits must be positive");
    }
    if (workerBits + datacenterBits + sequenceBits > 63) {
      throw new IllegalArgumentException("total bits must be <= 63");
    }
    this.workerBits = workerBits;
    this.datacenterBits = datacenterBits;
    this.sequenceBits = sequenceBits;
    return this;
  }

  public SnowflakeBuilder rollbackPolicy(ClockRollbackPolicy policy) {
    this.rollbackPolicy = Objects.requireNonNull(policy);
    return this;
  }

  /**
   * AUTO 策略下：允许的最大“轻微回退”时长（毫秒）。
   * 默认 5ms；视环境时间源与 NTP 调整幅度可适当放宽。
   */
  public SnowflakeBuilder maxRollbackMs(long ms) {
    if (ms < 0) {
      throw new IllegalArgumentException("maxRollbackMs must be >= 0");
    }
    this.maxRollbackMs = ms;
    return this;
  }

  /**
   * 是否在每个新毫秒的首个序列随机化（默认 true，可弱化偶数偏置与热点）。
   */
  public SnowflakeBuilder randomizeFirstSequence(boolean enable) {
    this.randomizeFirstSequence = enable;
    return this;
  }

  public Snowflake build() {
    return new Snowflake(this);
  }
}
