@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package cn.labzen.tool.util

import cn.labzen.tool.bean.Pair
import cn.labzen.tool.definition.Numbers
import cn.labzen.tool.exception.RandomException
import cn.labzen.tool.kotlin.throwRuntimeIf
import java.awt.Color
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object Randoms {

  const val NUMBERS = "0123456789"
  const val NUMBERS_WITHOUT_ZERO = "123456789"

  const val LETTERS_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz"
  const val LETTERS_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  const val LETTERS = LETTERS_LOWER_CASE + LETTERS_UPPER_CASE

  const val NUMBERS_AND_LETTERS = NUMBERS + LETTERS
  const val NUMBERS_AND_LETTERS_LOWER_CASE = NUMBERS + LETTERS_LOWER_CASE
  const val NUMBERS_AND_LETTERS_UPPER_CASE = NUMBERS + LETTERS_UPPER_CASE

  const val HEX_LOWER_CASE = "0123456789abcdef"
  const val HEX_UPPER_CASE = "0123456789ABCDEF"

  private const val MAX_COLOR_RANGE = 255

  /**
   * 获取随机整数
   * @param min 随机范围最小值，默认0
   * @param max 随机范围最大值
   * @param numbers 指定随机数字的类型[Numbers]，有效值： INTEGER, EVEN_NUMBER, ODD_NUMBER ，分别代表：范围内所有整数，范围内所有偶数，范围内所有奇数
   */
  @JvmStatic
  @JvmOverloads
  fun intNumber(min: Int = 0, max: Int, numbers: Numbers = Numbers.INTEGER): Int {
    val random = ThreadLocalRandom.current()
    return when (numbers) {
      Numbers.INTEGER -> random.nextInt(min, max)
      Numbers.EVEN_NUMBER -> random.nextInt(min, max) and -2
      Numbers.ODD_NUMBER -> random.nextInt(min, max) or 1
      else -> throw RandomException("numbers参数取值只支持INTEGER, EVEN_NUMBER, ODD_NUMBER")
    }
  }

  /**
   * 获取随机长整数（型）
   * @param min 随机范围最小值，默认0
   * @param max 随机范围最大值
   * @param numbers 指定随机数字的类型[Numbers]，有效值： INTEGER, EVEN_NUMBER, ODD_NUMBER ，分别代表：范围内所有整数，范围内所有偶数，范围内所有奇数
   */
  @JvmStatic
  @JvmOverloads
  fun longNumber(min: Long = 0, max: Long, numbers: Numbers = Numbers.INTEGER): Long {
    val random = ThreadLocalRandom.current()
    return when (numbers) {
      Numbers.INTEGER -> random.nextLong(min, max)
      Numbers.EVEN_NUMBER -> random.nextLong(min, max) and -2
      Numbers.ODD_NUMBER -> random.nextLong(min, max) or 1
      else -> throw RandomException("numbers参数取值只支持INTEGER, EVEN_NUMBER, ODD_NUMBER")
    }
  }

  /**
   * 生成随机字符串，默认使用[Randoms.NUMBERS_AND_LETTERS]作为随机候选字符集
   * ```
   * Randoms.string(10);
   * Randoms.string(10, Randoms.NUMBERS);
   * ```
   * 预置候选字符：
   * - [Randoms.NUMBERS]
   * - [Randoms.NUMBERS_WITHOUT_ZERO]
   * - [Randoms.LETTERS_LOWER_CASE]
   * - [Randoms.LETTERS_UPPER_CASE]
   * - [Randoms.LETTERS]
   * - [Randoms.NUMBERS_AND_LETTERS]
   * - [Randoms.NUMBERS_AND_LETTERS_LOWER_CASE]
   * - [Randoms.NUMBERS_AND_LETTERS_UPPER_CASE]
   * @param length Int 生成字符串长度
   * @param chars String 随机字符串的候选字符，默认[Randoms.NUMBERS_AND_LETTERS]
   * @return String 随机字符串
   */
  @JvmStatic
  @JvmOverloads
  fun string(length: Int, chars: String = NUMBERS_AND_LETTERS): String {
    if (length <= 0 || chars.isEmpty()) return ""

    val random = ThreadLocalRandom.current()
    val cs = CharArray(length)
    val size = chars.length
    for (i in 0 until length) {
      cs[i] = chars[random.nextInt(size)]
    }

    return String(cs)
  }

  /**
   * 生成随机字节数组
   */
  @JvmStatic
  fun bytes(length: Int): ByteArray {
    val random = ThreadLocalRandom.current()
    return ByteArray(length).apply {
      random.nextBytes(this)
    }
  }

  /**
   * 从[Collection]集合中检出随机元素
   * ```
   * ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
   * Randoms.element(list);
   * ```
   * @param target Collection<E> 集合
   * @return E 随机元素
   */
  @JvmStatic
  fun <E> element(target: Collection<E>): E {
    val random = ThreadLocalRandom.current()
    return target.let { target.elementAt(random.nextInt(it.size)) }
  }

  /**
   * 从[Map]集合中检出随机元素（随机Entry，并转换为键值对[Pair]）
   * ```
   * Map<String, Integer> map = Maps.newHashMap();
   * map.put("a", 1);
   * map.put("b", 2);
   * map.put("c", 3);
   * Pair<String, Integer> pair = Randoms.element(map);
   * ```
   * @param target Map<K, V> 集合
   * @return Pair<K, V> 保存Key，Value的键值对[Pair]
   */
  @JvmStatic
  fun <K, V> element(target: Map<K, V>): Pair<K, V> {
    val element = element(target.entries)
    return Pair(element.key, element.value)
  }

  /**
   * 随机RGB颜色
   * ```
   * Color color = Randoms.color();
  ```
   * @return Color [Color]
   */
  @JvmStatic
  fun rgbColor(): Color {
    val random = ThreadLocalRandom.current()
    return Color(random.nextInt(MAX_COLOR_RANGE), random.nextInt(MAX_COLOR_RANGE), random.nextInt(MAX_COLOR_RANGE))
  }

  /**
   * 随机十六进制颜色
   * ```
   * String colorHex = Randoms.colorHex();
   * ```
   * @return String #000000 十六进制颜色
   */
  @JvmStatic
  fun hexColor(): String {
    val random = ThreadLocalRandom.current()
    return String.format(
      "#%02x%02x%02x",
      random.nextInt(MAX_COLOR_RANGE),
      random.nextInt(MAX_COLOR_RANGE)
    )
  }

  /**
   * 随机时间，将返回在第一个和第二个参数时间质检的一个随机时间，如果某一个参数为空，则默认为当前时间。
   * 时间范围range中的两个时间不能同时为空，并且要保证第一个时间必须在第二个时间之前（如果第一个时间为空，
   * 则第二个时间必须在当前时间之后，反之亦然）
   * ```
   * Pair<Date, Date> range = new Pair<>(beginDate, endDate);
   * Date date = Randoms.date(range);
   * ```
   * @param range Pair<Date, Date> 时间范围，不能同时为空
   * @return Date 随机时间
   */
  @JvmStatic
  fun date(range: Pair<Date, Date>): Date =
    with(range) {
      val date = localDateTime(Pair(
        this.first?.let { DateTimes.toLocalDateTime(it) },
        this.second?.let { DateTimes.toLocalDateTime(it) }
      ))
      DateTimes.toDate(date)
    }

  /**
   * 随机时间，将返回在第一个和第二个参数时间质检的一个随机时间，如果某一个参数为空，则默认为当前时间。
   * 时间范围range中的两个时间不能同时为空，并且要保证第一个时间必须在第二个时间之前（如果第一个时间为空，
   * 则第二个时间必须在当前时间之后，反之亦然）
   * ```
   * Pair<LocalDateTime, LocalDateTime> range = new Pair<>(beginLocalDateTime, endLocalDateTime);
   * LocalDateTime localDateTime = Randoms.localDateTime(range);
   * ```
   * @param range Pair<LocalDateTime, LocalDateTime> 时间范围，不能同时为空
   * @return LocalDateTime 随机时间
   */
  @JvmStatic
  fun localDateTime(range: Pair<LocalDateTime, LocalDateTime>): LocalDateTime {
    val random = ThreadLocalRandom.current()
    return with(range) {
      (first == null && second == null).throwRuntimeIf { RandomException("range中至少要提供first, second任意一个时间点") }

      val baseline = if (first != null && second != null) {
        (first.isAfter(second)).throwRuntimeIf { RandomException("range中的second时间必须在first之后") }
        Pair(first, second)
      } else {
        Pair(
          first?.apply {
            (this.isAfter(LocalDateTime.now())).throwRuntimeIf { RandomException("range中的first时间在不提供second的情况下，必须在当前时间之前") }
          } ?: LocalDateTime.now(),
          second?.apply {
            (this.isBefore(LocalDateTime.now())).throwRuntimeIf { RandomException("range中的second时间在不提供first的情况下，必须在当前时间之后") }
          } ?: LocalDateTime.now()
        )
      }

      val duration = Duration.between(baseline.first, baseline.second)
      val randomSeconds = random.nextLong(duration.seconds)

      baseline.first!!.plusSeconds(randomSeconds)
    }
  }
}
