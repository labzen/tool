@file:Suppress("unused")

package cn.labzen.tool.feature

import cn.labzen.tool.exception.ArgumentsException
import cn.labzen.tool.kotlin.throwRuntimeIf
import org.slf4j.LoggerFactory

/**
 * 获取基于**Snowflake**算法的ID
 * ( *参考 http://git.oschina.net/yu120/sequence , kotlin重写，感谢* )
 *
 * > 基于Twitter的Snowflake算法实现分布式高效有序ID生产黑科技(sequence)。 SnowFlake的结构如下(每部分用-分开):
 *
 * > **0-0000000000 0000000000 0000000000 0000000000 0-00000-00000-000000000000**
 *
 *  1. 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 *  2. 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截) 得到的值，
 *      这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序 BASELINE 常量）。
 *  3. 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
 *  4. 10位的数据机器位，可以部署在1024个节点，包括5位dataCenterId和5位workerId
 *  5. 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 *
 * > 加起来刚好64位，为一个Long型。
 *
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 *
 * @param dataCenterId Int 数据中心id，取值 0 - 31
 * @param workerId Int 工作组id，取值 0 - 31
 * @author [Dean Zhao](mailto:rcarlosdasilva@qq.com)
 */
class Snowflake constructor(dataCenterId: Long, workerId: Long) {

  private val logger = LoggerFactory.getLogger(Snowflake::class.java)

  private val host: Host

  /**
   * 毫秒内序列(0~4095)
   */
  private var sequence = 0L

  /**
   * 上次生成ID的时间截
   */
  private var lastTimestamp = -1L

  init {
    (dataCenterId !in 0..MAX_DATA_CENTER_ID).throwRuntimeIf { ArgumentsException("[ID] - DataCenterId的取值超出了范围，默认0 - 31") }
    (workerId !in 0..MAX_WORKER_ID).throwRuntimeIf { ArgumentsException("[ID] - WorkerId的取值超出了范围，默认0 - 31") }

    host = Host(dataCenterId.shl(DATA_CENTER_ID_SHIFT), workerId.shl(WORKER_ID_SHIFT))

    logger.info("[ID] - ID序列化参数：DataCenterId: ${host.dataCenterId}, WorkerId: ${host.workerId}")
  }

  /**
   * 阻塞到下一个毫秒，直到获得新的时间戳
   *
   * @param lastTimestamp 上次生成ID的时间截
   * @return 当前时间戳
   */
  private fun until(lastTimestamp: Long): Long {
    var ts: Long
    do {
      ts = SystemClock.now()
    } while (ts < lastTimestamp)
    return ts
  }

  /**
   * 获取id
   *
   * @return id
   */
  @Synchronized
  fun id(): Long {
    var timestamp = SystemClock.now()

    // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
    // 闰秒
    if (timestamp < lastTimestamp) {
      val offset = lastTimestamp - timestamp
      (offset > 5).throwRuntimeIf { ArgumentsException("[ID] - 时钟回退，距当前时间：$offset 毫秒") }

      try {
        Thread.sleep(offset.shl(1))
        timestamp = SystemClock.now()

        (timestamp < lastTimestamp).throwRuntimeIf { ArgumentsException("[ID] - 时钟回退，距当前时间：$offset 毫秒") }
      } catch (e: InterruptedException) {
        throw ArgumentsException("[ID] - 时钟回退，调整等待时异常", e)
      }
    }

    // $NON-NLS-解决跨毫秒生成ID序列号始终为偶数的缺陷$
    // 如果是同一时间生成的，则进行毫秒内序列
    sequence = if (lastTimestamp == timestamp) {
      sequence = (sequence + 1) and SEQUENCE_MASK
      // 毫秒内序列溢出
      if (sequence == 0L)
        until(lastTimestamp) // 阻塞到下一个毫秒,获得新的时间戳
      else sequence
    } else 0L // 时间戳改变，毫秒内序列重置

    // 上次生成ID的时间截
    lastTimestamp = timestamp

    // 移位并通过或运算拼到一起组成64位的ID
    return (timestamp - BASELINE).shl(TIMESTAMP_LEFT_SHIFT) or host.dataCenterId or host.workerId or sequence
  }

  /**
   * @property dataCenterId dataCenterId - 数据中心ID(0~31)
   * @property workerId workerId - 工作机器ID(0~31)
   */
  internal data class Host(val dataCenterId: Long, val workerId: Long)

  companion object {
    /**
     * 时间起始标记点（2015年1月1日0时0分0秒），作为基准，一般取系统的最近时间（一旦确定不能变动）
     */
    private const val BASELINE = 1420041600000L

    /**
     * 机器id所占的位数
     */
    private const val WORKER_ID_BITS = 5

    /**
     * 数据标识id所占的位数
     */
    private const val DATA_CENTER_ID_BITS = 5

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private const val MAX_WORKER_ID = (-1L).shl(WORKER_ID_BITS).inv()

    /**
     * 支持的最大数据标识id，结果是31
     */
    private const val MAX_DATA_CENTER_ID = (-1L).shl(DATA_CENTER_ID_BITS).inv()

    /**
     * 序列在id中占的位数
     */
    private const val SEQUENCE_BITS = 12

    /**
     * 机器ID向左移12位
     */
    private const val WORKER_ID_SHIFT = SEQUENCE_BITS

    /**
     * 数据标识id向左移17位(12+5)
     */
    private const val DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS

    /**
     * 时间截向左移22位(5+5+12)
     */
    private const val TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private const val SEQUENCE_MASK = (-1L).shl(SEQUENCE_BITS).inv()
  }

}
