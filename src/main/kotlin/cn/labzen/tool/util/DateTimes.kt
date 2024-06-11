package cn.labzen.tool.util

import cn.labzen.tool.definition.Constants.Companion.PATTERN_OF_DATE_TIME
import cn.labzen.tool.util.DateTimes.DateTimeDifferenceSymbols.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs
import kotlin.properties.Delegates

object DateTimes {

  private val SYSTEM_TIME_ZONE = ZoneId.systemDefault()
  private val DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_OF_DATE_TIME)
  private val DEFAULT_SIMPLE_DATE_FORMAT = SimpleDateFormat(PATTERN_OF_DATE_TIME)

  /**
   * 将[Date]转换为[LocalDateTime]
   * @param date Date
   * @return LocalDateTime
   */
  @JvmStatic
  fun toLocalDateTime(date: Date) = LocalDateTime.ofInstant(date.toInstant(), SYSTEM_TIME_ZONE)!!

  /**
   * 将[Date]转换为[LocalDate]
   * @param date Date
   * @return LocalDate
   */
  @JvmStatic
  fun toLocalDate(date: Date) = toLocalDateTime(date).toLocalDate()!!

  /**
   * 将[Date]转换为[LocalTime]
   * @param date Date
   * @return LocalTime
   */
  @JvmStatic
  fun toLocalTime(date: Date) = toLocalDateTime(date).toLocalTime()!!

  /**
   * 将[LocalDateTime]转换为[Date]
   * @param ldt LocalDateTime
   * @return Date
   */
  @JvmStatic
  fun toDate(ldt: LocalDateTime) = Date.from(ldt.atZone(SYSTEM_TIME_ZONE).toInstant())!!

  /**
   * 将[LocalDate] / [LocalTime]转换为[Date]
   * @param ld LocalDate
   * @param lt LocalTime?
   * @return Date
   */
  @JvmStatic
  @JvmOverloads
  fun toDate(ld: LocalDate, lt: LocalTime? = null) =
    lt?.let { Date.from(LocalDateTime.of(ld, lt).atZone(SYSTEM_TIME_ZONE).toInstant())!! }
      ?: Date.from(ld.atStartOfDay().atZone(SYSTEM_TIME_ZONE).toInstant())!!

  /**
   * 获取当前时间字符串，默认格式：yyyy-MM-dd HH:mm:ss
   */
  @JvmStatic
  @JvmOverloads
  fun formatNow(pattern: String? = null): String =
    LocalDateTime.now().format(pattern?.let { DateTimeFormatter.ofPattern(it) } ?: DEFAULT_DATE_TIME_FORMATTER)

  /**
   * 将时间格式化为 yyyy-MM-dd HH:mm:ss 格式字符串
   */
  fun format(ldt: LocalDateTime): String =
    ldt.format(DEFAULT_DATE_TIME_FORMATTER)

  /**
   * 将时间格式化为 yyyy-MM-dd HH:mm:ss 格式字符串
   */
  @JvmStatic
  @Synchronized
  fun format(date: Date): String =
    DEFAULT_SIMPLE_DATE_FORMAT.format(date)

  /**
   * 计算参数时间与当前时间的相差时长
   *
   * [pattern]参数有效字符，每个符号只需一个字符表示：
   *
   * | Symbol | Meaning | Examples |
   * | ------ | ------- | -------- |
   * | y      | 相差几年 | "相差 y 年" => "相差 4 年" |
   * | M      | 相差几月 | "相差 M 月" => "相差 7 月" |
   * | d      | 相差几天 | "相差 d 天" => "相差 10 天" |
   * | w      | 相差几周 | "相差 w 周" => "相差 4 周" |
   * | H      | 相差几小时 | "相差 H 小时" => "相差 23 小时" |
   * | m      | 相差几分钟 | "相差 m 分钟" => "相差 30 分钟" |
   * | s      | 相差几秒钟 | "相差 s 秒钟" => "相差 55 秒钟" |
   * | -      | optional 不进位 | 参考备注 1 |
   * | ()?    | optional 不显示 | 参考备注 2 |
   * | c(&brvbar;) | optional 表前后关系 | 参考备注 3 |
   *
   * —————————————————————————————————————————————————
   *
   * **备注 1：符号'-'，适用于'除s外的字母'，仅当适配低一级单位缺失，且低一级单位不满足高一级单位的进位时，用以标识该单位进位，默认进位；**
   * ```java
   * LocalDateTime time; // 例如：该时间与当前时间差实际为50分钟
   *
   * DateTimes.howLong(time, "相差 H 小时"); // 格式化后为 "相差 1 小时"
   * DateTimes.howLong(time, "相差 H- 小时"); // 格式化后为 "相差 0 小时"
   * DateTimes.howLong(time, "相差 H 小时 m 分钟"); // 格式化后为 "相差 0小时 50 分钟"，这里字符'H'带不带'-'效果都一样
   * ```
   *
   * **备注 2：符号'()?'，适用于当前单位为0时，是否显示英文括号'()'包含的这一段字符串，有且只能有一个单位符号，不包含单位符号时，'()?'将原样输出；**
   * ```java
   * LocalDateTime time1; // 例如：该时间与当前时间差实际为50分钟
   * LocalDateTime time2; // 例如：该时间与当前时间差实际为70分钟
   *
   * DateTimes.howLong(time1, "相差 (H 小时 )?m 分钟"); // 格式化后为 "相差 50 分钟"
   * DateTimes.howLong(time2, "相差 (H 小时 )?m 分钟"); // 格式化后为 "相差 1小时 10 分钟"
   * DateTimes.howLong(time2, "相差 (d 天 H 小时 )?m 分钟"); // XXX 不能包含多个单位符号，只能解析第一个符号，其余的符号将原样输出
   * DateTimes.howLong(time1, "相差 (H 小时)?"); // 格式化后为 "相差 1小时"
   * DateTimes.howLong(time1, "相差 (H- 小时)?"); // 格式化后为 "相差 "
   * ```
   *
   * **备注 3：符号'c(&brvbar;)'，用于表达时间的前后关系，当参数时间在当前时间之后，将取值字符英文括号内字符'&brvbar;'之后的字符串，否则去之前的字符串；
   * 如果按照格式化字符串中的符号级别算，最后是0，即无时间差，则该符号不显示。**
   * ```java
   * LocalDateTime time1; // 例如：该时间在当前时间之后5小时
   * LocalDateTime time2; // 例如：该时间在当前时间之前5小时
   *
   * DateTimes.howLong(time1, "H 小时c(前|后)"); // 格式化后为 "5 小时后"
   * DateTimes.howLong(time2, "H 小时c(前|后)"); // 格式化后为 "5 小时前"
   * ```
   *
   * @param ldt 相对于当前时间，需要计算时差的另一个时间
   * @param pattern 时差格式化字符串
   */
  @JvmStatic
  fun howLong(ldt: LocalDateTime, pattern: String) =
    howLong(LocalDateTime.now(), ldt, pattern)

  /**
   * 计算第一个时间与第二个时间的相差时长
   *
   * [pattern]参数有效字符，每个符号只需一个字符表示：
   *
   * | Symbol | Meaning | Examples |
   * | ------ | ------- | -------- |
   * | y      | 相差几年 | "相差 y 年" => "相差 4 年" |
   * | M      | 相差几月 | "相差 M 月" => "相差 7 月" |
   * | d      | 相差几天 | "相差 d 天" => "相差 10 天" |
   * | w      | 相差几周 | "相差 w 周" => "相差 4 周" |
   * | H      | 相差几小时 | "相差 H 小时" => "相差 23 小时" |
   * | m      | 相差几分钟 | "相差 m 分钟" => "相差 30 分钟" |
   * | s      | 相差几秒钟 | "相差 s 秒钟" => "相差 55 秒钟" |
   * | -      | optional 不进位 | 参考备注 1 |
   * | ()?    | optional 不显示 | 参考备注 2 |
   * | c(&brvbar;) | optional 表前后关系 | 参考备注 3 |
   *
   * —————————————————————————————————————————————————
   *
   * **备注 1：符号'-'，适用于'除s外的字母'，仅当适配低一级单位缺失，且低一级单位不满足高一级单位的进位时，用以标识该单位进位，默认进位；**
   * ```java
   * LocalDateTime based; // 时差基准时间
   * LocalDateTime time; // 例如：该时间与基准时间差实际为50分钟
   *
   * DateTimes.howLong(based, time, "相差 H 小时"); // 格式化后为 "相差 1 小时"
   * DateTimes.howLong(based, time, "相差 H- 小时"); // 格式化后为 "相差 0 小时"
   * DateTimes.howLong(based, time, "相差 H 小时 m 分钟"); // 格式化后为 "相差 0小时 50 分钟"，这里字符'H'带不带'-'效果都一样
   * ```
   *
   * **备注 2：符号'()?'，适用于当前单位为0时，是否显示英文括号'()'包含的这一段字符串，有且只能有一个单位符号，不包含单位符号时，'()?'将原样输出；**
   * ```java
   * LocalDateTime based; // 时差基准时间
   * LocalDateTime time1; // 例如：该时间与基准时间差实际为50分钟
   * LocalDateTime time2; // 例如：该时间与基准时间差实际为70分钟
   *
   * DateTimes.howLong(based, time1, "相差 (H 小时 )?m 分钟"); // 格式化后为 "相差 50 分钟"
   * DateTimes.howLong(based, time2, "相差 (H 小时 )?m 分钟"); // 格式化后为 "相差 1小时 10 分钟"
   * DateTimes.howLong(based, time2, "相差 (d 天 H 小时 )?m 分钟"); // XXX 不能包含多个单位符号，只能解析第一个符号，其余的符号将原样输出
   * DateTimes.howLong(based, time1, "相差( H 小时)?"); // 格式化后为 "相差 1小时"
   * DateTimes.howLong(based, time1, "相差( H- 小时)?"); // 格式化后为 "相差"
   * DateTimes.howLong(based, time1, "相差( H- 小时)?"); // 格式化后为 "相差"
   * ```
   *
   * **备注 3：符号'c(&brvbar;)'，用于表达时间的前后关系，当参数时间在当前时间之后，将取值字符英文括号内字符'&brvbar;'之后的字符串，否则去之前的字符串；
   * 如果按照格式化字符串中的符号级别算，最后是0，即无时间差，则该符号不显示。**
   * ```java
   * LocalDateTime based; // 时差基准时间
   * LocalDateTime time1; // 例如：该时间在基准时间之后5小时
   * LocalDateTime time2; // 例如：该时间在基准时间之前5小时
   *
   * DateTimes.howLong(based, time1, "H 小时c(前|后)"); // 格式化后为 "5 小时后"
   * DateTimes.howLong(based, time2, "H 小时c(前|后)"); // 格式化后为 "5 小时前"
   * ```
   *
   * @param first 第一个时间，基准时间
   * @param second 相对于第一个时间，需要计算时差的另一个时间
   * @param pattern 时差格式化字符串
   */
  @JvmStatic
  fun howLong(first: LocalDateTime, second: LocalDateTime, pattern: String): String {
    val milliFirst = first.atZone(SYSTEM_TIME_ZONE).toInstant().toEpochMilli()
    val milliSecond = second.atZone(SYSTEM_TIME_ZONE).toInstant().toEpochMilli()

    val parsedPatternSymbols = parseHowLongPattern(pattern).sortedBy { it.level }

    var surplus: Long = abs(milliSecond - milliFirst)
    var latestSymbol: DateTimeDifferenceSegment? = null
    for (symbol in parsedPatternSymbols) {
      if (symbol is DateTimeDifferenceSymbolBeforeOrAfter) {
        symbol.value = milliFirst - milliSecond
        continue
      }
      if (symbol.level < 0) {
        continue
      }

      if (latestSymbol != null && symbol.level == latestSymbol.level) {
        symbol.value = latestSymbol.value
        continue
      }

      when (symbol.level) {
        YEAR.level -> {
          symbol.value = surplus / YEAR.duration
          surplus %= YEAR.duration
        }

        MONTH.level -> {
          symbol.value = surplus / MONTH.duration
          surplus %= MONTH.duration
        }

        WEEK.level -> {
          symbol.value = surplus / WEEK.duration
          surplus %= WEEK.duration
        }

        DAY.level -> {
          symbol.value = surplus / DAY.duration
          surplus %= DAY.duration
        }

        HOUR.level -> {
          symbol.value = surplus / HOUR.duration
          surplus %= HOUR.duration
        }

        MINUTE.level -> {
          symbol.value = surplus / MINUTE.duration
          surplus %= MINUTE.duration
        }

        SECOND.level -> symbol.value = surplus
      }

      latestSymbol = symbol
    }

    return parsedPatternSymbols.sortedBy { it.order }.joinToString("") { it.toString() }
  }

  private fun parseHowLongPattern(pattern: String): List<DateTimeDifferenceSegment> {
    var index = 0
    val symbols = mutableListOf<DateTimeDifferenceSegment>()
    var order = 0
    while (index < pattern.length) {
      val symbol = readNextSymbolOrChars(index, pattern)
      symbol.order = order++

      index = symbol.nextIndex
      symbols.add(symbol)
    }
    return symbols
  }

  private val SYMBOLS = arrayOf('y', 'M', 'd', 'w', 'H', 'm', 's', 'c', '(')
  private fun readNextSymbolOrChars(index: Int, pattern: String): DateTimeDifferenceSegment {
    var nextPosition = index

    while (nextPosition < pattern.length) {
      when (val char = pattern[nextPosition++]) {
        '\\' -> nextPosition++ // 防止对字符的转义，错误的当做格式化字符处理
        's' -> return DateTimeDifferenceSymbol("s", nextPosition, SECOND.level)
        'y', 'M', 'd', 'w', 'H', 'm' -> {
          val isRounding = pattern[nextPosition] == '-'
          if (isRounding) nextPosition++
          val segment = pattern.substring(index, nextPosition)
          val symbol = symbol(char)!!
          return DateTimeDifferenceSymbol(segment, nextPosition, symbol.level, isRounding)
        }

        'c' -> {
          // 是否符合时间前后关系的字符格式
          if (pattern[nextPosition] == '(') {
            val baaEnd = pattern.indexOf(')', nextPosition)
            if (baaEnd > 0) {
              nextPosition = baaEnd + 1
              val segment = pattern.substring(index, nextPosition)
              val values = segment.drop(2).dropLast(1).split("|")
              return DateTimeDifferenceSymbolBeforeOrAfter(
                segment,
                nextPosition,
                values[0],
                if (values.size < 2) "" else values[1]
              )
            }
          }
        }

        '(' -> {
          val optionalEnd = pattern.indexOf(")?", nextPosition)
          if (optionalEnd > 0) {
            val segment = pattern.substring(index, optionalEnd)
            val valueString = segment.drop(1).dropLast(2)
            val embedded = parseHowLongPattern(valueString)
            if (embedded.isNotEmpty()) {
              val single = embedded.singleOrNull { it is DateTimeDifferenceSymbol } as DateTimeDifferenceSymbol?
              if (single != null) {
                return DateTimeDifferenceSymbolOptional(segment, nextPosition, single.level, embedded)
              }
            }
          }
        }

        else -> if (pattern[nextPosition] in SYMBOLS) break
      }
    }

    val segment = pattern.substring(index, nextPosition)
    return DateTimeDifferenceSegment(segment, nextPosition)
  }

  private open class DateTimeDifferenceSegment(val segment: String, val nextIndex: Int, val level: Int = -1) {
    var order by Delegates.notNull<Int>()
    var value by Delegates.notNull<Long>()
    override fun toString(): String = segment
  }

  private class DateTimeDifferenceSymbol(segment: String, nextIndex: Int, level: Int, val rounding: Boolean = false) :
    DateTimeDifferenceSegment(segment, nextIndex, level) {
    override fun toString(): String = value.toString()
  }

  private class DateTimeDifferenceSymbolOptional(
    segment: String,
    nextIndex: Int,
    level: Int,
    val embedded: List<DateTimeDifferenceSegment>
  ) : DateTimeDifferenceSegment(segment, nextIndex, level) {
    override fun toString(): String =
      embedded.joinToString { it.toString() }
  }

  private class DateTimeDifferenceSymbolBeforeOrAfter(
    segment: String,
    nextIndex: Int,
    val beforeString: String,
    val afterString: String
  ) : DateTimeDifferenceSegment(segment, nextIndex) {
    override fun toString(): String =
      if (value > 0) beforeString else afterString
  }

  private enum class DateTimeDifferenceSymbols(val level: Int, val char: Char, val duration: Long) {
    YEAR(0, 'y', 31_536_000_000), // 1000 * 60 * 60 * 24 * 365
    MONTH(1, 'M', 2_592_000_000), // 1000 * 60 * 60 * 24 * 30
    WEEK(2, 'w', 604_800_000), // 1000 * 60 * 60 * 24 * 7
    DAY(3, 'd', 86_400_000), // 1000 * 60 * 60 * 24
    HOUR(4, 'H', 3_600_000), // 1000 * 60 * 60
    MINUTE(5, 'm', 60_000 * 60), // 1000 * 60
    SECOND(6, 's', 1000)
  }

  private fun symbol(char: Char): DateTimeDifferenceSymbols? {
    return DateTimeDifferenceSymbols.values().find { char == it.char }
  }
}
