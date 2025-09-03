package cn.labzen.tool.util;

import java.util.Arrays;
import java.util.function.Supplier;

public final class Objects {

  private static final int DEFAULT_RADIX = 10;

  private Objects() {
  }

  /**
   * 参数全部为空
   */
  public static boolean isAllNull(Object... objs) {
    return Arrays.stream(objs).allMatch(java.util.Objects::isNull);
  }

  /**
   * 参数全部不为空
   */
  public static boolean isAllNotNull(Object... objs) {
    return Arrays.stream(objs).allMatch(java.util.Objects::nonNull);
  }

  /**
   * 参数要么全为空，要么全不为空
   */
  public static boolean isAllNullOrNot(Object... objs) {
    return isAllNotNull(objs) || isAllNull(objs);
  }

  /**
   * 参数中有空值
   */
  public static boolean isAnyNull(Object... objs) {
    return Arrays.stream(objs).anyMatch(java.util.Objects::isNull);
  }

  /**
   * 参数中有非空值
   */
  public static boolean isAnyNotNull(Object... objs) {
    return Arrays.stream(objs).anyMatch(java.util.Objects::nonNull);
  }

  /**
   * 参数中的第几项为空
   */
  public static boolean isNullAt(int index, Object... objs) {
    return objs.length > index && objs[index] == null;
  }

  /**
   * 第一个参数为空，第二个参数不为空
   */
  public static boolean isLeftNull(Object left, Object right) {
    return left == null && right != null;
  }

  /**
   * 第一个参数不为空，第二个参数为空
   */
  public static boolean isRightNull(Object left, Object right) {
    return left != null && right == null;
  }

  /**
   * 返回第一个不为空的对象，如果全部为空，返回 null
   */
  public static Object firstNonNull(Object... objs) {
    if (objs == null) {
      return null;
    }
    for (Object obj : objs) {
      if (obj != null) {
        return obj;
      }
    }
    return null;
  }

  /**
   * 判断相等，否则抛异常
   */
  public static void equalsOrThrow(Object first, Object second, Supplier<? extends Exception> supplier) throws
      Exception {
    if (!java.util.Objects.equals(first, second)) {
      throw supplier.get();
    }
  }

  /**
   * 判断相等，否则执行某段代码
   */
  public static Object equalsOrElse(Object first, Object second, Supplier<?> function) {
    if (!java.util.Objects.equals(first, second)) {
      return function.get();
    }
    return null;
  }

  /**
   * 不为空，否则抛异常
   */
  public static void notNullOrThrow(Object o, Supplier<? extends Exception> supplier) throws Exception {
    if (o == null) {
      throw supplier.get();
    }
  }

  /**
   * 尝试转为 int，不可转时返回 null 或 def
   */
  public static Integer canBeInt(String s) {
    return canBeInt(s, null);
  }

  public static Integer canBeInt(String s, Integer def) {
    Long result = canBeLong(s, def == null ? null : def.longValue());
    if (result != null && result == result.intValue()) {
      return result.intValue();
    }
    return def;
  }

  /**
   * 尝试转为 long，不可转时返回 null 或 def
   */
  public static Long canBeLong(String s) {
    return canBeLong(s, null);
  }

  public static Long canBeLong(String s, Long def) {
    if (s == null || s.isBlank()) {
      return def;
    }

    boolean negative = s.charAt(0) == '-';
    int index = negative ? 1 : 0;
    if (index == s.length()) {
      return def;
    }

    int digit = AsciiDigits.digit(s.charAt(index++));
    if (digit < 0 || digit >= DEFAULT_RADIX) {
      return def;
    }
    long accum = -digit;

    long cap = Long.MIN_VALUE / DEFAULT_RADIX;

    while (index < s.length()) {
      digit = AsciiDigits.digit(s.charAt(index++));
      if (digit < 0 || digit >= DEFAULT_RADIX || accum < cap) {
        return def;
      }
      accum *= DEFAULT_RADIX;
      if (accum < Long.MIN_VALUE + digit) {
        return def;
      }
      accum -= digit;
    }

    if (negative) {
      return accum;
    } else if (accum == Long.MIN_VALUE) {
      return def;
    } else {
      return -accum;
    }
  }

  /**
   * 内部 ASCII 数字解析工具
   */
  static final class AsciiDigits {

    private static final byte[] asciiDigits = new byte[128];

    static {
      Arrays.fill(asciiDigits, (byte) -1);
      for (int i = 0; i <= 9; i++) {
        asciiDigits['0' + i] = (byte) i;
      }
      for (int i = 0; i <= 26; i++) {
        asciiDigits['A' + i] = (byte) (10 + i);
        asciiDigits['a' + i] = (byte) (10 + i);
      }
    }

    static int digit(char c) {
      return c < 128 ? asciiDigits[c] : -1;
    }
  }

}
