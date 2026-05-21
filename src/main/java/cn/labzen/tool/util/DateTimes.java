package cn.labzen.tool.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.labzen.tool.definition.Constants.PATTERN_OF_DATE_TIME;

public final class DateTimes {

  private static final ZoneId SYSTEM_TIME_ZONE = ZoneId.systemDefault();
  private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_OF_DATE_TIME);
  private static final char[] SYMBOLS = new char[]{'y', 'M', 'd', 'w', 'H', 'm', 's', 'c', '('};
  private static final Cache<String, DateTimeFormatter> DATETIME_FORMATTER_CACHE = Caffeine.newBuilder()
                                                                                           .maximumSize(50)
                                                                                           .expireAfterAccess(1,
                                                                                               TimeUnit.HOURS)
                                                                                           .build();
  private static final Cache<String, SimpleDateFormat> SIMPLE_DATE_FORMAT_CACHE = Caffeine.newBuilder()
                                                                                          .maximumSize(10)
                                                                                          .expireAfterAccess(1,
                                                                                              TimeUnit.HOURS)
                                                                                          .build();

  private DateTimes() {
  }

  /**
   * 将Date转换为LocalDateTime
   */
  public static LocalDateTime toLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), SYSTEM_TIME_ZONE);
  }

  /**
   * 将Date转换为LocalDate
   */
  public static LocalDate toLocalDate(Date date) {
    return toLocalDateTime(date).toLocalDate();
  }

  /**
   * 将Date转换为LocalTime
   */
  public static LocalTime toLocalTime(Date date) {
    return toLocalDateTime(date).toLocalTime();
  }

  /**
   * 将LocalDateTime转换为Date
   */
  public static Date toDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(SYSTEM_TIME_ZONE).toInstant());
  }

  /**
   * 将LocalDate / LocalTime转换为Date
   */
  public static Date toDate(LocalDate localDate, LocalTime localTime) {
    return Date.from(LocalDateTime.of(localDate, localTime).atZone(SYSTEM_TIME_ZONE).toInstant());
  }

  /**
   * 当前时间字符串，默认格式：yyyy-MM-dd HH:mm:ss
   */
  public static String formattedNow() {
    return LocalDateTime.now().format(DEFAULT_DATE_TIME_FORMATTER);
  }

  /**
   * 当前时间字符串
   */
  public static String formattedNow(String pattern) {
    DateTimeFormatter formatter = DATETIME_FORMATTER_CACHE.get(pattern, DateTimeFormatter::ofPattern);
    return LocalDateTime.now().format(formatter);
  }

  /**
   * 将时间格式化为 yyyy-MM-dd HH:mm:ss 格式字符串
   */
  public static String format(LocalDateTime localDateTime) {
    assert localDateTime != null;
    return localDateTime.format(DEFAULT_DATE_TIME_FORMATTER);
  }

  /**
   * 格式化时间字符串
   */
  public static String format(LocalDateTime localDateTime, String pattern) {
    assert localDateTime != null && pattern != null;
    DateTimeFormatter formatter = DATETIME_FORMATTER_CACHE.get(pattern, DateTimeFormatter::ofPattern);
    return localDateTime.format(formatter);
  }

  /**
   * 将时间格式化为 yyyy-MM-dd HH:mm:ss 格式字符串
   */
  public static String format(Date date) {
    assert date != null;
    SimpleDateFormat formatter = SIMPLE_DATE_FORMAT_CACHE.get(PATTERN_OF_DATE_TIME, SimpleDateFormat::new);
    return formatter.format(date);
  }

  /**
   * 格式化时间字符串
   */
  public static String format(Date date, String pattern) {
    assert date != null && pattern != null;
    SimpleDateFormat formatter = SIMPLE_DATE_FORMAT_CACHE.get(pattern, SimpleDateFormat::new);
    return formatter.format(date);
  }

  // ===================================================================================================================

  /**
   * 计算参数时间与当前时间的相差时长
   * <p>
   * pattern参数有效字符，每个符号只需一个字符表示：
   * <table>
   *   <tr><th>符号</th><th>含义</th><th>pattern示例</th></tr>
   *   <tr><td>y</td><td>相差几年</td><td>"相差 y 年" => "相差 4 年"</td></tr>
   *   <tr><td>M</td><td>相差几月</td><td>"相差 M 月" => "相差 7 月"</td></tr>
   *   <tr><td>d</td><td>相差几天</td><td>"相差 d 天" => "相差 10 天"</td></tr>
   *   <tr><td>w</td><td>相差几周</td><td>"相差 w 周" => "相差 4 周"</td></tr>
   *   <tr><td>H</td><td>相差几小时</td><td>"相差 H 小时" => "相差 23 小时"</td></tr>
   *   <tr><td>m</td><td>相差几分钟</td><td>"相差 m 分钟" => "相差 30 分钟"</td></tr>
   *   <tr><td>s</td><td>相差几秒钟</td><td>"相差 s 秒钟" => "相差 55 秒钟"</td></tr>
   *   <tr><td>-</td><td>optional 不进位</td><td>参考备注 1</td></tr>
   *   <tr><td>()?</td><td>optional 不显示</td><td>参考备注 2</td></tr>
   *   <tr><td>c(&brvbar;)</td><td>optional 表前后关系</td><td>参考备注 3</td></tr>
   * </table>
   * <hr/>
   * <b>备注 1：</b>符号'-'，适用于'除s外的字母'，仅当适配低一级单位缺失，且低一级单位不满足高一级单位的进位时，用以标识该单位进位，默认进位；
   * <code>
   * <pre>
   * LocalDateTime time; // 例如：该时间与当前时间差实际为50分钟
   *
   * DateTimes.howLong(time, "相差 H 小时"); // 格式化后为 "相差 1 小时"
   * DateTimes.howLong(time, "相差 H- 小时"); // 格式化后为 "相差 0 小时"
   * DateTimes.howLong(time, "相差 H 小时 m 分钟"); // 格式化后为 "相差 0小时 50 分钟"，这里字符'H'带不带'-'效果都一样
   * </pre>
   * </code>
   * <hr/>
   * <b>备注 2：</b>符号'()?'，适用于当前单位为0时，是否显示英文括号'()'包含的这一段字符串，有且只能有一个单位符号，不包含单位符号时，'()?'将原样输出；
   * <code>
   * <pre>
   * LocalDateTime time1; // 例如：该时间与当前时间差实际为50分钟
   * LocalDateTime time2; // 例如：该时间与当前时间差实际为70分钟
   *
   * DateTimes.howLong(time1, "相差 (H 小时 )?m 分钟"); // 格式化后为 "相差 50 分钟"
   * DateTimes.howLong(time2, "相差 (H 小时 )?m 分钟"); // 格式化后为 "相差 1小时 10 分钟"
   * DateTimes.howLong(time2, "相差 (d 天 H 小时 )?m 分钟"); // XXX 不能包含多个单位符号，只能解析第一个符号，其余的符号将原样输出
   * DateTimes.howLong(time1, "相差 (H 小时)?"); // 格式化后为 "相差 1小时"
   * DateTimes.howLong(time1, "相差 (H- 小时)?"); // 格式化后为 "相差 "
   * </pre>
   * </code>
   * <hr/>
   * <b>备注 3：</b>符号'c(¦)'，用于表达时间的前后关系，当参数时间在当前时间之后，将取值字符英文括号内字符'¦'之后的字符串，否则去之前的字符串； 如果按照格式化字符串中的符号级别算，最后是0，即无时间差，则该符号不显示。
   * <code>
   * <pre>
   * LocalDateTime time1; // 例如：该时间在当前时间之后5小时
   * LocalDateTime time2; // 例如：该时间在当前时间之前5小时
   *
   * DateTimes.howLong(time1, "H 小时c(前|后)"); // 格式化后为 "5 小时后"
   * DateTimes.howLong(time2, "H 小时c(前|后)"); // 格式化后为 "5 小时前"
   * </pre>
   * </code>
   *
   * @param localDateTime 相对于当前时间，需要计算时差的另一个时间
   * @param pattern       时差格式化字符串
   */
  public static String howLong(LocalDateTime localDateTime, String pattern) {
    return howLong(LocalDateTime.now(), localDateTime, pattern);
  }

  /**
   * 计算第一个时间与第二个时间的相差时长
   *
   * @param first   第一个时间，基准时间
   * @param second  相对于第一个时间，需要计算时差的另一个时间
   * @param pattern 时差格式化字符串
   * @see DateTimes#howLong(LocalDateTime, String)
   */
  public static String howLong(LocalDateTime first, LocalDateTime second, String pattern) {
    long milliFirst = first.atZone(SYSTEM_TIME_ZONE).toInstant().toEpochMilli();
    long milliSecond = second.atZone(SYSTEM_TIME_ZONE).toInstant().toEpochMilli();

    List<DateTimeDifferenceSegment> parsedPatternSymbols = parseHowLongPattern(pattern);
    parsedPatternSymbols.sort(Comparator.comparingInt(DateTimeDifferenceSegment::level));

    long surplus = Math.abs(milliSecond - milliFirst);
    DateTimeDifferenceSegment latestSymbol = null;

    for (DateTimeDifferenceSegment symbol : parsedPatternSymbols) {
      if (symbol instanceof DateTimeDifferenceSymbolBeforeOrAfter baa) {
        baa.setValue(milliFirst - milliSecond);
        continue;
      }
      if (symbol.level < 0) {
        continue;
      }

      if (latestSymbol != null && symbol.level == latestSymbol.level) {
        symbol.setValue(latestSymbol.value);
        continue;
      }

      switch (symbol.level) {
        case 0 -> { // YEAR
          symbol.setValue(surplus / DateTimeDifferenceSymbols.YEAR.duration);
          surplus %= DateTimeDifferenceSymbols.YEAR.duration;
        }
        case 1 -> { // MONTH
          symbol.setValue(surplus / DateTimeDifferenceSymbols.MONTH.duration);
          surplus %= DateTimeDifferenceSymbols.MONTH.duration;
        }
        case 2 -> { // WEEK
          symbol.setValue(surplus / DateTimeDifferenceSymbols.WEEK.duration);
          surplus %= DateTimeDifferenceSymbols.WEEK.duration;
        }
        case 3 -> { // DAY
          symbol.setValue(surplus / DateTimeDifferenceSymbols.DAY.duration);
          surplus %= DateTimeDifferenceSymbols.DAY.duration;
        }
        case 4 -> { // HOUR
          symbol.setValue(surplus / DateTimeDifferenceSymbols.HOUR.duration);
          surplus %= DateTimeDifferenceSymbols.HOUR.duration;
        }
        case 5 -> { // MINUTE
          symbol.setValue(surplus / DateTimeDifferenceSymbols.MINUTE.duration);
          surplus %= DateTimeDifferenceSymbols.MINUTE.duration;
        }
        case 6 -> // SECOND
            symbol.setValue(surplus);
      }
      latestSymbol = symbol;
    }

    parsedPatternSymbols.sort(Comparator.comparingInt(DateTimeDifferenceSegment::order));
    StringBuilder sb = new StringBuilder();
    for (DateTimeDifferenceSegment s : parsedPatternSymbols) {
      sb.append(s);
    }
    return sb.toString();
  }

  private static List<DateTimeDifferenceSegment> parseHowLongPattern(String pattern) {
    int index = 0;
    List<DateTimeDifferenceSegment> symbols = new ArrayList<>();
    AtomicInteger order = new AtomicInteger(0);

    while (index < pattern.length()) {
      DateTimeDifferenceSegment symbol = readNextSymbolOrChars(index, pattern);
      symbol.setOrder(order.getAndIncrement());
      index = symbol.nextIndex;
      symbols.add(symbol);
    }
    return symbols;
  }

  private static DateTimeDifferenceSegment readNextSymbolOrChars(int index, String pattern) {
    int nextPosition = index;

    while (nextPosition < pattern.length()) {
      char ch = pattern.charAt(nextPosition++);
      switch (ch) {
        case '\\' -> nextPosition++; // 跳过转义
        case 's' -> {
          return new DateTimeDifferenceSymbol("s", nextPosition, DateTimeDifferenceSymbols.SECOND.level, false);
        }
        case 'y', 'M', 'd', 'w', 'H', 'm' -> {
          boolean isRounding = nextPosition < pattern.length() && pattern.charAt(nextPosition) == '-';
          if (isRounding) {
            nextPosition++;
          }
          String segment = pattern.substring(index, nextPosition);
          DateTimeDifferenceSymbols symbol = symbol(ch);
          assert symbol != null;
          return new DateTimeDifferenceSymbol(segment, nextPosition, symbol.level, isRounding);
        }
        case 'c' -> {
          if (nextPosition < pattern.length() && pattern.charAt(nextPosition) == '(') {
            int baaEnd = pattern.indexOf(')', nextPosition);
            if (baaEnd > 0) {
              nextPosition = baaEnd + 1;
              String segment = pattern.substring(index, nextPosition);
              String[] values = segment.substring(2, segment.length() - 1).split("\\|");
              return new DateTimeDifferenceSymbolBeforeOrAfter(segment,
                  nextPosition,
                  values[0],
                  values.length < 2 ? "" : values[1]);
            }
          }
        }
        case '(' -> {
          int optionalEnd = pattern.indexOf(")?", nextPosition);
          if (optionalEnd > 0) {
            String segment = pattern.substring(index, optionalEnd);
            String valueString = segment.substring(1, segment.length() - 2);
            List<DateTimeDifferenceSegment> embedded = parseHowLongPattern(valueString);
            if (!embedded.isEmpty()) {
              Optional<DateTimeDifferenceSegment> singleOpt = embedded.stream()
                                                                      .filter(e -> e instanceof DateTimeDifferenceSymbol)
                                                                      .findFirst();
              if (singleOpt.isPresent()) {
                return new DateTimeDifferenceSymbolOptional(segment, nextPosition, singleOpt.get().level, embedded);
              }
            }
          }
        }
        default -> {
          if (nextPosition < pattern.length() && new String(SYMBOLS).indexOf(pattern.charAt(nextPosition)) >= 0) {
            break;
          }
        }
      }
    }
    String segment = pattern.substring(index, nextPosition);
    return new DateTimeDifferenceSegment(segment, nextPosition);
  }

  // ---------------------- 内部类 ----------------------

  private static class DateTimeDifferenceSegment {

    protected final String segment;
    protected final int nextIndex;
    protected final int level;
    protected int order;
    protected long value;

    public DateTimeDifferenceSegment(String segment, int nextIndex) {
      this(segment, nextIndex, -1);
    }

    public DateTimeDifferenceSegment(String segment, int nextIndex, int level) {
      this.segment = segment;
      this.nextIndex = nextIndex;
      this.level = level;
    }

    public void setOrder(int order) {
      this.order = order;
    }

    public int order() {
      return order;
    }

    public int level() {
      return level;
    }

    public void setValue(long value) {
      this.value = value;
    }

    public long value() {
      return value;
    }

    @Override
    public String toString() {
      return segment;
    }
  }

  private static class DateTimeDifferenceSymbol extends DateTimeDifferenceSegment {

    private final boolean rounding;

    public DateTimeDifferenceSymbol(String segment, int nextIndex, int level, boolean rounding) {
      super(segment, nextIndex, level);
      this.rounding = rounding;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  private static class DateTimeDifferenceSymbolOptional extends DateTimeDifferenceSegment {

    private final List<DateTimeDifferenceSegment> embedded;

    public DateTimeDifferenceSymbolOptional(String segment,
                                            int nextIndex,
                                            int level,
                                            List<DateTimeDifferenceSegment> embedded) {
      super(segment, nextIndex, level);
      this.embedded = embedded;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (DateTimeDifferenceSegment e : embedded) {
        sb.append(e);
      }
      return sb.toString();
    }
  }

  private static class DateTimeDifferenceSymbolBeforeOrAfter extends DateTimeDifferenceSegment {

    private final String beforeString;
    private final String afterString;

    public DateTimeDifferenceSymbolBeforeOrAfter(String segment,
                                                 int nextIndex,
                                                 String beforeString,
                                                 String afterString) {
      super(segment, nextIndex);
      this.beforeString = beforeString;
      this.afterString = afterString;
    }

    @Override
    public String toString() {
      return value > 0 ? beforeString : afterString;
    }
  }

  private enum DateTimeDifferenceSymbols {
    YEAR(0, 'y', 31_536_000_000L),
    MONTH(1, 'M', 2_592_000_000L),
    WEEK(2, 'w', 604_800_000L),
    DAY(3, 'd', 86_400_000L),
    HOUR(4, 'H', 3_600_000L),
    MINUTE(5, 'm', 60_000L),
    SECOND(6, 's', 1000L);

    final int level;
    final char ch;
    final long duration;

    DateTimeDifferenceSymbols(int level, char ch, long duration) {
      this.level = level;
      this.ch = ch;
      this.duration = duration;
    }
  }

  private static DateTimeDifferenceSymbols symbol(char c) {
    for (DateTimeDifferenceSymbols s : DateTimeDifferenceSymbols.values()) {
      if (s.ch == c) {
        return s;
      }
    }
    return null;
  }
}
