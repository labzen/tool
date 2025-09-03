package cn.labzen.tool.util;

import cn.labzen.tool.definition.Number;
import cn.labzen.tool.exception.RandomException;
import cn.labzen.tool.structure.Pair;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class Randoms {

  public static final String NUMBERS = "0123456789";
  public static final String NUMBERS_WITHOUT_ZERO = "123456789";

  public static final String LETTERS_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
  public static final String LETTERS_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static final String LETTERS = LETTERS_LOWER_CASE + LETTERS_UPPER_CASE;
  public static final String NUMBERS_AND_LETTERS = NUMBERS + LETTERS;
  public static final String NUMBERS_AND_LETTERS_LOWER_CASE = NUMBERS + LETTERS_LOWER_CASE;
  public static final String NUMBERS_AND_LETTERS_UPPER_CASE = NUMBERS + LETTERS_UPPER_CASE;

  public static final String HEX_LOWER_CASE = "0123456789abcdef";
  public static final String HEX_UPPER_CASE = "0123456789ABCDEF";

  private static final int MAX_COLOR_RANGE = 255;

  private Randoms() {
  }

  /**
   * 获取随机整数
   */
  public static int intNumber(int min, int max, Number number) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    return switch (number) {
      case INTEGER -> random.nextInt(min, max);
      case EVEN_NUMBER -> random.nextInt(min, max) & -2;
      case ODD_NUMBER -> random.nextInt(min, max) | 1;
      default -> throw new RandomException("numbers参数取值只支持INTEGER, EVEN_NUMBER, ODD_NUMBER");
    };
  }

  public static int intNumber(int max) {
    return intNumber(0, max, Number.INTEGER);
  }

  public static int intNumber(int min, int max) {
    return intNumber(min, max, Number.INTEGER);
  }

  /**
   * 获取随机长整数
   */
  public static long longNumber(long min, long max, Number number) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    return switch (number) {
      case INTEGER -> random.nextLong(min, max);
      case EVEN_NUMBER -> random.nextLong(min, max) & -2;
      case ODD_NUMBER -> random.nextLong(min, max) | 1;
      default -> throw new RandomException("numbers参数取值只支持INTEGER, EVEN_NUMBER, ODD_NUMBER");
    };
  }

  public static long longNumber(long max) {
    return longNumber(0, max, Number.INTEGER);
  }

  public static long longNumber(long min, long max) {
    return longNumber(min, max, Number.INTEGER);
  }

  /**
   * 生成随机字符串
   */
  public static String string(int length, String chars) {
    if (length <= 0 || chars == null || chars.isEmpty()) {
      return "";
    }
    ThreadLocalRandom random = ThreadLocalRandom.current();
    char[] cs = new char[length];
    int size = chars.length();
    for (int i = 0; i < length; i++) {
      cs[i] = chars.charAt(random.nextInt(size));
    }
    return new String(cs);
  }

  public static String string(int length) {
    return string(length, NUMBERS_AND_LETTERS);
  }

  /**
   * 生成随机字节数组
   */
  public static byte[] bytes(int length) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    byte[] result = new byte[length];
    random.nextBytes(result);
    return result;
  }

  /**
   * 从集合中随机取一个元素
   */
  public static <E> E element(Collection<E> target) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    int index = random.nextInt(target.size());
    if (target instanceof List<E> list) {
      return list.get(index);
    } else {
      int i = 0;
      for (E e : target) {
        if (i == index) {
          return e;
        }
        i++;
      }
      throw new IllegalStateException("集合遍历失败");
    }
  }

  /**
   * 从 Map 中随机取一个 Entry 并返回 Pair
   */
  public static <K, V> Pair<K, V> element(Map<K, V> target) {
    Map.Entry<K, V> entry = element(target.entrySet());
    return new Pair<>(entry.getKey(), entry.getValue());
  }

  /**
   * 随机 RGB 颜色
   */
  public static Color rgbColor() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    return new Color(random.nextInt(MAX_COLOR_RANGE), random.nextInt(MAX_COLOR_RANGE), random.nextInt(MAX_COLOR_RANGE));
  }

  /**
   * 随机十六进制颜色
   */
  public static String hexColor() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    return String.format("#%02x%02x%02x",
        random.nextInt(MAX_COLOR_RANGE),
        random.nextInt(MAX_COLOR_RANGE),
        random.nextInt(MAX_COLOR_RANGE));
  }

  /**
   * 随机时间（Date）
   */
  public static Date date(Pair<Date, Date> range) {
    LocalDateTime dateTime = localDateTime(new Pair<>(range.first() !=
                                                      null ? DateTimes.toLocalDateTime(range.first()) : null,
        range.second() != null ? DateTimes.toLocalDateTime(range.second()) : null));
    return DateTimes.toDate(dateTime);
  }

  /**
   * 随机时间（LocalDateTime）
   */
  public static LocalDateTime localDateTime(Pair<LocalDateTime, LocalDateTime> range) {
    ThreadLocalRandom random = ThreadLocalRandom.current();

    if (range.first() == null && range.second() == null) {
      throw new RandomException("range中至少要提供first, second任意一个时间点");
    }

    Pair<LocalDateTime, LocalDateTime> baseline;
    if (range.first() != null && range.second() != null) {
      if (range.first().isAfter(range.second())) {
        throw new RandomException("range中的second时间必须在first之后");
      }
      baseline = new Pair<>(range.first(), range.second());
    } else {
      LocalDateTime first = range.first();
      LocalDateTime second = range.second();

      if (first != null && first.isAfter(LocalDateTime.now())) {
        throw new RandomException("range中的first时间在不提供second的情况下，必须在当前时间之前");
      }
      if (second != null && second.isBefore(LocalDateTime.now())) {
        throw new RandomException("range中的second时间在不提供first的情况下，必须在当前时间之后");
      }

      baseline = new Pair<>(first != null ? first : LocalDateTime.now(), second != null ? second : LocalDateTime.now());
    }

    Duration duration = Duration.between(baseline.first(), baseline.second());
    long randomSeconds = random.nextLong(duration.getSeconds());
    return baseline.first().plusSeconds(randomSeconds);
  }
}
