package cn.labzen.tool.util;

import cn.labzen.tool.exception.StringException;
import cn.labzen.tool.structure.Pair;
import com.google.common.base.Ascii;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Strings {

  private Strings() {
  }

  /**
   * 如果source为null，返回defaultValue。否则返回source
   */
  public static String value(String source, String defaultValue) {
    return source == null ? defaultValue : source;
  }

  /**
   * 如果source为null（或Blank String），返回defaultValue。否则返回source
   *
   * @param includeBlank 是否包含Blank String的情况，为true时，source是blank string，会返回defaultValue
   */
  public static String value(String source, String defaultValue, boolean includeBlank) {
    if (includeBlank && isBlank(source)) {
      return defaultValue;
    }
    return source == null ? defaultValue : source;
  }

  /**
   * 如果source为null，返回defaultValue。否则返回String.valueOf(source)
   */
  public static String value(Object source, String defaultValue) {
    return source == null ? defaultValue : String.valueOf(source);
  }

  /**
   * 如果参数source字符串值，匹配pattern，则返回replacement，否则返回source
   *
   * @param pattern     正则表达式
   * @param replacement 替代字符串
   * @return 匹配pattern的字符串替换为replacement，否则原样返回
   */
  public static String value(String source, String pattern, String replacement) {
    if (source != null && source.matches(pattern)) {
      return replacement;
    } else {
      return source;
    }
  }

  /**
   * 如果字符串为empty（null或空字符串""），返回参数defaultValue
   */
  public static String valueWhenEmpty(String source, String defaultValue) {
    return isEmpty(source) ? defaultValue : source;
  }

  /**
   * 如果字符串为blank（null或空字符串""或空白字符串" "），返回参数defaultValue
   */
  public static String valueWhenBlank(String source, String defaultValue) {
    return isBlank(source) ? defaultValue : source;
  }

  // ===================================================================================================================

  /**
   * 判断字符串是否为null或empty
   */
  public static boolean isEmpty(String source) {
    return source == null || source.isEmpty();
  }

  /**
   * 判断字符串是否不为null或empty
   */
  public static boolean isNotEmpty(String source) {
    return !isEmpty(source);
  }

  /**
   * 判断字符串是否为null或blank
   */
  public static boolean isBlank(String source) {
    return source == null || source.trim().isEmpty();
  }

  /**
   * 判断字符串是否不为null或blank
   */
  public static boolean isNotBlank(String source) {
    return !isBlank(source);
  }

  /**
   * 检查给出的字符串集合中，是否有任意一个为null或空字符
   * <code>
   * <pre>
   * // return true
   * Strings.isAnyBlank("string", null, "", "  ");
   * // return true
   * Strings.isAnyBlank("string", "notNull", "  ");
   * // return true
   * Strings.isAnyBlank("string", "notNull", "");
   * // return true
   * Strings.isAnyBlank("string", "notNull", null);
   * // return false
   * Strings.isAnyBlank("string", "notNull", "notBlank");
   * </pre>
   * </code>
   *
   * @return 任意一个字符串是blank则为true
   */
  public static boolean isAnyBlank(String... sources) {
    return isAnyBlank(Arrays.asList(sources));
  }

  /**
   * 检查给出的字符串集合中，是否有任意一个为null或空字符
   *
   * @return 任意一个字符串是blank则为true
   * @see Strings#isAnyBlank(String...)
   */
  public static boolean isAnyBlank(List<String> sources) {
    return sources == null || sources.isEmpty() || sources.stream().anyMatch(Strings::isBlank);
  }

  /**
   * 检查给出的字符串集合中，是不是所有的字符串均为为null或空字符
   * <code>
   * <pre>
   * // return true
   * Strings.isAllBlank("", null, "", "  ");
   * // return false
   * Strings.isAllBlank("string", "notNull", "  ");
   * // return false
   * Strings.isAllBlank("string", "notNull", "");
   * // return false
   * Strings.isAllBlank("string", "notNull", null);
   * // return false
   * Strings.isAllBlank("string", "notNull", "notBlank");
   * </pre>
   * </code>
   *
   * @return 所有的字符串都是blank则为true
   */
  public static boolean isAllBlank(String... sources) {
    return isAllBlank(Arrays.asList(sources));
  }

  /**
   * 检查给出的字符串集合中，是不是所有的字符串均为为null或空字符
   *
   * @return 所有的字符串都是blank则为true
   * @see Strings#isAllBlank(String...)
   */
  public static boolean isAllBlank(List<String> sources) {
    return sources == null || sources.isEmpty() || sources.stream().allMatch(Strings::isBlank);
  }

  /**
   * 如果字符串为empty（null或空字符串""），返回null
   */
  public static String emptyToNull(String source) {
    return isEmpty(source) ? null : source;
  }

  /**
   * 如果字符串为blank（null或空字符串“”或空白字符串" "），返回null
   */
  public static String blankToNull(String source) {
    return isBlank(source) ? null : source;
  }

  // ===================================================================================================================

  /**
   * 连接多个字符串，null值将被忽略
   * <code>
   * <pre>
   * // return "123";
   * Strings.concat("1", "", "2", null, "3");
   * </pre>
   * </code>
   */
  public static String concat(String... sources) {
    return concat(Arrays.asList(sources));
  }

  /**
   * 连接多个字符串，null值将被忽略
   * <code>
   * <pre>
   * List<String> strings = List.of("1", "", "2", null, "3");
   * // return "123";
   * Strings.concat(strings);
   * </pre>
   * </code>
   */
  public static String concat(List<String> sources) {
    return sources.stream().filter(java.util.Objects::nonNull).collect(Collectors.joining(""));
  }

  // ===================================================================================================================

  /**
   * 对source字符串两边，修剪掉指定的字符串redundant
   *
   * @see Strings#trim(String, String, Position)
   */
  public static String trim(String source, String redundant) {
    return trim(source, redundant, Position.BOTH);
  }

  /**
   * 对source字符串修剪掉指定的字符串redundant
   * <code>
   * <pre>
   * // return "123"
   * Strings.trim("===123===", "=", Position.BOTH);
   * // return "123==="
   * Strings.trim("===123===", "=", Position.LEFT);
   * // return "===123"
   * Strings.trim("===123===", "=", Position.RIGHT);
   * </pre>
   * </code>
   *
   * @param source    原字符串
   * @param redundant 需要去掉的字符串
   * @param position  去掉的位置，左边或右边，或两边
   */
  public static String trim(String source, String redundant, Position position) {
    if (isEmpty(redundant)) {
      return source;
    }

    int length = source.length();
    int step = redundant.length();
    int start = 0;
    int end = length;

    if (position == Position.BOTH || position == Position.LEFT) {
      while (start < end && source.indexOf(redundant, start) == start) {
        start += step;
      }
    }

    if (position == Position.BOTH || position == Position.RIGHT) {
      while (start < end && source.lastIndexOf(redundant, end - 1) == (end - step)) {
        end -= step;
      }
    }

    return source.substring(start, end);
  }

  // ===================================================================================================================

  /**
   * 获取指定下标的单个字符，index为负数时，从后向前找，index从0开始计数，0返回左侧第一个字符，-1返回最后一个字符
   * <code>
   * <pre>
   * // return "4";
   * Strings.at("123456789", 3);
   * // return "6";
   * Strings.at("123456789", -4);
   * </pre>
   * </code>
   */
  public static char at(String source, int index) {
    if (isEmpty(source)) {
      throw new IllegalArgumentException("expect non-empty string");
    }

    int length = source.length();
    if (Math.abs(index) >= length) {
      throw new IllegalArgumentException("index out of range");
    }

    int i;
    if (index < 0) {
      i = length + index;
    } else {
      i = index;
    }

    return source.charAt(i);
  }

  /**
   * 截取从指定下标开始之前/之后的子字符串。start为截取的开始下标，为负数时，下标从源字符串最后向前确认开始截取的下标。length决定子字符串的长度，当length为负数时，代表从后向前截取
   * <code>
   * <pre>
   * // return "345"
   * Strings.sub("0123456789", 3, 3);
   * // return "123"
   * Strings.sub("0123456789", 3, -3);
   * // return "789"
   * Strings.sub("0123456789", -3, 3);
   * // return "567"
   * Strings.sub("0123456789", -3, -3);
   * </pre>
   * </code>
   *
   * @param start  开始截取子字符串的下标，如是负数则从字符串的后面开始计数，-1则是下标为最后一个字符的位置
   * @param length 截取子字符串的长度，负数则向前截取，整数向后截取
   * @throws StringException 如果截取子字符串的长度超过可截取范围，则抛出异常
   */
  public static String sub(String source, int start, int length) throws StringException {
    if (isEmpty(source) || length == 0) {
      return "";
    }
    if (Math.abs(length) >= source.length()) {
      throw new IllegalArgumentException("length out of range");
    }

    boolean forward = length < 0;
    int pos1, pos2;
    if (start < 0) {
      pos1 = source.length() + start;
    } else {
      pos1 = start;
    }
    pos2 = pos1 + length;

    if ((forward && pos2 < 0) || (!forward && pos2 > source.length())) {
      throw new StringException("字符串截取开始下标越界：[size={}, start={}, end={}]", source.length(), pos2, pos1);
    }

    if (forward) {
      return source.substring(pos2 + 1, pos1 + 1);
    } else {
      return source.substring(pos1, pos2);
    }
  }

  /**
   * 获取指定的字符区间的内容
   * <code>
   * <pre>
   * // return List.of("abc", "def")
   * Strings.between("[abc] xyz [def]", "[", "]");
   * </pre>
   * </code>
   *
   * @param start 开始字符，不能与end相同
   * @param end   结束字符，不能与start相同
   */
  public static List<String> between(String source, char start, char end) {
    List<String> result = new ArrayList<>();
    StringBuilder tmp = null;
    boolean found = false;

    for (char c : source.toCharArray()) {
      if (!found && c == start) {
        tmp = new StringBuilder();
        found = true;
      } else if (found && c == end) {
        result.add(tmp.toString());
        tmp = null;
        found = false;
      } else if (tmp != null) {
        tmp.append(c);
      }
    }

    return result;
  }

  /**
   * 按分隔符切分字符串为两部分，如果找不到分隔符，返回null
   * <code>
   * <pre>
   * // return Pair("a", "b")
   * Strings.cut("a-b", "-")
   * // return Pair("localhost", "8080")
   * Strings.cut("localhost:8080", ":")
   * </pre>
   * </code>
   *
   * @param separator 分隔符，如果分隔符在source中出现超过一次，则从第一次出现的位置分割
   * @return 分隔符两遍的字符串
   */
  public static Pair<String, String> cut(String source, String separator) {
    int start = source.indexOf(separator);
    if (start == -1) {
      return null;
    }

    String first = source.substring(0, start);
    String second = source.substring(start + separator.length());
    return new Pair<>(first, second);
  }

  // ===================================================================================================================

  private static final char DELIMITER_START = '{';
  private static final String DELIMITER_STR = "{}";
  private static final char ESCAPE_CHAR = '\\';

  /**
   * 格式化字符串，使用可变数量参数代替掉字符串中出现的{}
   * <code>
   * <pre>
   * // return "a=1,b=2,a+b=3"
   * Strings.format("a={},b={},a+b={}", "1", "2", "3")
   * // return "a=1,b=2,a+b=\\{3}"
   * Strings.format("a={},b={},a+b=\\{{}}", "1", "2", "3")
   * // return "a=1,b=2,a+b={}3"
   * Strings.format("a={},b={},a+b=\\{}{}", "1", "2", "3")
   * // return "a=1,b=2,a+b=\\3"
   * Strings.format("a={},b={},a+b=\\\\{}", "1", "2", "3")
   * // return "a={},b={},a+b={}"
   * Strings.format("a={},b={},a+b={}")
   * // return "a=1,b=2,a+b=3"
   * Strings.format("a={},b={},a+b={}", "1", "2", "3", "4")
   * // return "a=1,b=2,a+b={}"
   * Strings.format("a={},b={},a+b={}", "1", "2")
   * // return "a=1,b=[null],a+b={}"
   * Strings.format("a={},b={},a+b={}", "1", null)
   * </pre>
   * </code>
   */
  public static String format(String source, Object... args) {
    return format(source, Arrays.asList(args));
  }

  /**
   * 格式化字符串，使用可变数量参数代替掉字符串中出现的{}
   *
   * @see Strings#format(String, Object...)
   */
  public static String format(String pattern, List<Object> args) {
    if (isEmpty(pattern) || args == null || args.isEmpty()) {
      return pattern;
    }
    int start = 0, current, ai = 0;
    StringBuilder buf = new StringBuilder();

    while (ai < args.size()) {
      Object arg = args.get(ai);
      current = pattern.indexOf(DELIMITER_STR, start);
      if (current == -1) {
        break;
      }

      boolean escaped = false;
      if (current > 0) {
        char c = at(pattern, current - 1);
        escaped = c == ESCAPE_CHAR;
      }
      if (escaped) {
        char c = at(pattern, current - 2);
        if (c == ESCAPE_CHAR) {
          buf.append(pattern, start, current - 1);
          buf.append(formatArguments(arg));
          start = current + 2;
        } else {
          ai--;
          buf.append(pattern, start, current - 1);
          buf.append(DELIMITER_START);
          start = current + 1;
        }
      } else {
        buf.append(pattern, start, current);
        buf.append(formatArguments(arg));
        start = current + 2;
      }

      ai++;
    }

    buf.append(pattern, start, pattern.length());
    return buf.toString();
  }

  private static String formatArguments(Object arg) {
    return arg == null ? "[null]" : String.valueOf(arg);
  }

  // ===================================================================================================================

  /**
   * 统计aim子字符串在source字符串中出现的次数。从source中查找子字符串出现的开始位置，是在上一次找到的位置下标 + 子字符串的长度。也即是说查找"aaa"中"aa"出现的次数，这里是1次，而不是2次，区分大小写
   *
   * @see Strings#times(String, String, boolean)
   */
  public static int times(String source, String aim) {
    return times(source, aim, true);
  }

  /**
   * 统计aim子字符串在source字符串中出现的次数。从source中查找子字符串出现的开始位置，是在上一次找到的位置下标 + 子字符串的长度。也即是说查找"aaa"中"aa"出现的次数，这里是1次，而不是2次
   * <code>
   * <pre>
   * // return 0
   * Strings.times("", "");
   * // return 0
   * Strings.times("abc", "1");
   * // return 1
   * Strings.times("aaa", "aa");
   * // return 1
   * Strings.times("abc123xyz", "123");
   * // return 2
   * Strings.times("xyz&abc&123", "&");
   * // return 2
   * Strings.times("Abc&abc&ABC&abC", "ab");
   * // return 4
   * Strings.times("Abc&abc&ABC&abC", "ab", false);
   * </pre>
   * </code>
   *
   * @return aim在source中出现的次数
   */
  public static int times(String source, String aim, boolean caseSensitive) {
    if (isEmpty(source) || isEmpty(aim)) {
      return 0;
    }

    if (!caseSensitive) {
      source = source.toLowerCase();
      aim = aim.toLowerCase();
    }

    int times = 0, position = 0;
    while (true) {
      position = source.indexOf(aim, position);
      if (position == -1) {
        break;
      }
      position += aim.length();
      times++;
    }
    return times;
  }

  // ===================================================================================================================

  /**
   * 清除字符串中单词间多余的空格（单词间只保留一个空格），以及两侧的空格
   * <code>
   * <pre>
   * // return "sample english sentence."
   * Strings.simplify("  sample   english   sentence.   ")
   * </pre>
   * </code>
   */
  public static String simplify(String source) {
    if (isEmpty(source)) {
      return source;
    }
    return source.replaceAll("\\s+", " ").trim();
  }

  // ===================================================================================================================

  private static String casing(String source, int start, int length, Function<Character, Character> caser) throws
      StringException {
    if (length < 0 || isEmpty(source)) {
      return source;
    }
    int size = source.length();
    if (Math.abs(length) > size) {
      throw new StringException("需要处理的长度大于源字符串");
    }

    int pos1, pos2;
    if (start < 0) {
      pos1 = start + size;
    } else {
      pos1 = start;
    }
    if (length == 0) {
      pos2 = size;
    } else {
      pos2 = pos1 + length;
    }

    if (pos2 > size) {
      throw new StringException("转换处理结束下标越界：[size={}, start={}, end={}]", size, pos1, pos2);
    }

    char[] chars = source.toCharArray();
    for (int i = pos1; i < pos2; i++) {
      chars[i] = caser.apply(chars[i]);
    }
    return new String(chars);
  }

  /**
   * 小写所有字符
   * <code>
   * <pre>
   * // return "abc"
   * Strings.toLowerCase("ABC", 0);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1, 1);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1, 2);
   * </pre>
   * </code>
   *
   * @param start 需要小写的字符（开始）下标，为负数从字符串尾部偏移下标
   */
  public static String toLowerCase(String source, int start) throws StringException {
    return casing(source, start, 0, Ascii::toLowerCase);
  }

  /**
   * 小写指定字符
   * <code>
   * <pre>
   * // return "abc"
   * Strings.toLowerCase("ABC", 0);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1, 1);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1, 2);
   * </pre>
   * </code>
   *
   * @param start  需要小写的字符（开始）下标，为负数从字符串尾部偏移下标
   * @param length 需要处理的长度，不能为负数，设为0（默认），处理至字符串尾部
   */
  public static String toLowerCase(String source, int start, int length) throws StringException {
    return casing(source, start, length, Ascii::toLowerCase);
  }

  /**
   * 大写所有字符
   * <code>
   * <pre>
   * // return "ABC"
   * Strings.toUpperCase("abc", 0);
   * // return "aBC"
   * Strings.toUpperCase("abc", 1);
   * // return "aBc"
   * Strings.toUpperCase("abc", 1, 1);
   * // return "aBC"
   * Strings.toUpperCase("abc", 1, 2);
   * </pre>
   * </code>
   *
   * @param start 需要大写的字符（开始）下标，为负数从字符串尾部偏移下标
   */
  public static String toUpperCase(String source, int start) throws StringException {
    return casing(source, start, 0, Ascii::toUpperCase);
  }

  /**
   * 大写指定字符
   * <code>
   * <pre>
   * // return "ABC"
   * Strings.toUpperCase("abc", 0);
   * // return "aBC"
   * Strings.toUpperCase("abc", 1);
   * // return "aBc"
   * Strings.toUpperCase("abc", 1, 1);
   * // return "aBC"
   * Strings.toUpperCase("abc", 1, 2);
   * </pre>
   * </code>
   *
   * @param start  需要大写的字符（开始）下标，为负数从字符串尾部偏移下标
   * @param length 需要处理的长度，不能为负数，设为0（默认），处理至字符串尾部
   */
  public static String toUpperCase(String source, int start, int length) throws StringException {
    return casing(source, start, length, Ascii::toUpperCase);
  }

  private static final Set<Character> SECTION_SPACE_CHARACTERS = Set.of(' ', '-', '_', '\r', '\n', '\t');

  private static List<String> slicingStrings(String source, Cases cases) {
    if (isEmpty(source)) {
      return java.util.Collections.emptyList();
    }

    List<String> result = new ArrayList<>();
    StringBuilder buf = new StringBuilder();
    int size = source.length(), i = 0;

    if (cases == null) {
      cases = Cases.KEEP;
    }

    while (i < size) {
      char c = source.charAt(i);
      if (SECTION_SPACE_CHARACTERS.contains(c)) {
        if (!buf.isEmpty()) {
          switch (cases) {
            case KEEP -> result.add(buf.toString());
            case LOWERCASE -> result.add(buf.toString().toLowerCase());
            case UPPERCASE -> result.add(buf.toString().toUpperCase());
          }
          buf.delete(0, buf.length());
        }

        while (i < size && SECTION_SPACE_CHARACTERS.contains(source.charAt(i))) {
          i++;
        }
        continue;
      }

      if (Ascii.isUpperCase(c) && !buf.isEmpty()) {
        switch (cases) {
          case KEEP -> result.add(buf.toString());
          case LOWERCASE -> result.add(buf.toString().toLowerCase());
          case UPPERCASE -> result.add(buf.toString().toUpperCase());
        }
        buf.delete(0, buf.length());
      }

      buf.append(c);
      i++;
    }

    // 添加最后一个
    if (!buf.isEmpty()) {
      switch (cases) {
        case KEEP -> result.add(buf.toString());
        case LOWERCASE -> result.add(buf.toString().toLowerCase());
        case UPPERCASE -> result.add(buf.toString().toUpperCase());
      }
    }

    return result;
  }

  /**
   * 转换成驼峰式，首字母大写
   * <code>
   * <pre>
   * // return "ThereIsAWord"
   * Strings.studlyCase("  there is a word"));
   * // return "ThereIsAWord"
   * Strings.studlyCase("there_is_a_word  "));
   * // return "ThereIsAWord"
   * Strings.studlyCase(" there-is-a-word "));
   * // return ""
   * Strings.studlyCase("   "));
   * </pre>
   * </code>
   */
  public static String studlyCase(String source) {
    return slicingStrings(source, Cases.KEEP).stream().map(w -> toUpperCase(w, 0, 1)).collect(Collectors.joining());
  }

  /**
   * 转换成驼峰式，首字母小写
   * <code>
   * <pre>
   * // return "thereIsAWord"
   * Strings.camelCase("  there is a word"));
   * // return "thereIsAWord"
   * Strings.camelCase("there_is_a_word  "));
   * // return "thereIsAWord"
   * Strings.camelCase(" there-is-a-word "));
   * // return ""
   * Strings.camelCase("   "));
   * </pre>
   * </code>
   */
  public static String camelCase(String source) {
    return toLowerCase(studlyCase(source), 0, 1);
  }

  /**
   * 转换成Snake Case，以下划线间隔
   * <code>
   * <pre>
   * // return "there_is_a_word"
   * Strings.snakeCase("  there is a word");
   * // return "there_is_a_word"
   * Strings.snakeCase("there_is_a_word  ");
   * // return "there_is_a_word"
   * Strings.snakeCase(" there-is-a-word ");
   * // return ""
   * Strings.snakeCase("   ");
   * </pre>
   * </code>
   */
  public static String snakeCase(String source) {
    return String.join("_", slicingStrings(source, Cases.KEEP));
  }

  /**
   * 转换成Snake Case，以下划线间隔
   * <code>
   * <pre>
   * // return "there_is_a_word"
   * Strings.snakeCase("  there is a word");
   * // return "there_is_a_word"
   * Strings.snakeCase("there_is_a_word  ");
   * // return "there_is_a_word"
   * Strings.snakeCase(" there-is-a-word ");
   * // return ""
   * Strings.snakeCase("   ");
   * </pre>
   * </code>
   *
   * @param cases 指定转换后的字符串大小写
   */
  public static String snakeCase(String source, Cases cases) {
    return String.join("_", slicingStrings(source, cases));
  }

  /**
   * 转换成Kebab Case，以短横线间隔
   * <code>
   * <pre>
   * // return "there-is-a-word"
   * Strings.kebabCase("  there is a word");
   * // return "there-is-a-word"
   * Strings.kebabCase("there_is_a_word  ");
   * // return "there-is-a-word"
   * Strings.kebabCase(" there-is-a-word ");
   * // return ""
   * Strings.kebabCase("   ");
   * </pre>
   * </code>
   */
  public static String kebabCase(String source) {
    return String.join("-", slicingStrings(source, Cases.KEEP));
  }

  /**
   * 转换成Kebab Case，以短横线间隔
   * <code>
   * <pre>
   * // return "there-is-a-word"
   * Strings.kebabCase("  there is a word");
   * // return "there-is-a-word"
   * Strings.kebabCase("there_is_a_word  ");
   * // return "there-is-a-word"
   * Strings.kebabCase(" there-is-a-word ");
   * // return ""
   * Strings.kebabCase("   ");
   * </pre>
   * </code>
   *
   * @param cases 指定转换后的字符串大小写
   */
  public static String kebabCase(String source, Cases cases) {
    return String.join("-", slicingStrings(source, cases));
  }

  // ===================================================================================================================

  /**
   * 重复给定的字符串直到字符串达到或超过给定长度
   * <code>
   * <pre>
   * // return "00000"
   * Strings.repeatUntil("0", 5);
   * // return "01010"
   * Strings.repeatUntil("01", 5);
   * // return "01201"
   * Strings.repeatUntil("012", 5);
   * </pre>
   * </code>
   */
  public static String repeatUntil(String source, int length) throws StringException {
    if (length < 0) {
      throw new StringException("重复的长度不能为负数");
    }
    if (length == 0 || isEmpty(source)) {
      return "";
    }

    int size = source.length();
    char[] array = new char[length];
    source.getChars(0, size, array, 0);

    int n = size;
    while (n < length - n) {
      System.arraycopy(array, 0, array, n, n);
      n <<= 1;
    }
    System.arraycopy(array, 0, array, n, length - n);
    return new String(array);
  }

  /**
   * 补充字符串达到给定长度， length如果为负数，将字符串填充至尾部。类似Guava的Strings.padStart()或Strings.padEnd()
   * <code>
   * <pre>
   * // return "binary numbers look like ..."
   * Strings.fill("binary numbers look like ", ".", -28);
   * // return "binary numbers look like 01010"
   * Strings.fill("binary numbers look like ", "01", -30);
   * // return "!@#!@#!binary numbers look like "
   * Strings.fill("binary numbers look like ", "!@#", 32);
   * </pre>
   * </code>
   *
   * @param filler 补充的字符串
   * @param length 最终长度，为负数时，将填充至字符串尾部，整数时，填充至字符串开头
   */
  public static String fill(String source, String filler, int length) {
    int len = Math.abs(length);
    if (len <= source.length() || isEmpty(filler)) {
      return source;
    }

    String pad = repeatUntil(filler, len - source.length());
    if (length < 0) {
      return source + pad;
    } else {
      return pad + source;
    }
  }

  // ===================================================================================================================

  /**
   * 检查字符串source中是否包含全部指定的子字符串集合fragments，允许重叠，区分大小写
   *
   * @see Strings#containsAll(String, boolean, boolean, Collection)
   */
  public static boolean containsAll(String source, String... fragments) {
    return containsAll(source, Arrays.asList(fragments));
  }

  /**
   * 检查字符串source中是否包含全部指定的子字符串集合fragments，允许重叠，区分大小写
   *
   * @see Strings#containsAll(String, boolean, boolean, Collection)
   */
  public static boolean containsAll(String source, Collection<String> fragments) {
    return containsAll(source, true, true, fragments);
  }

  /**
   * 检查字符串source中是否包含全部指定的子字符串集合fragments，区分大小写
   *
   * @see Strings#containsAll(String, boolean, boolean, Collection)
   */
  public static boolean containsAll(String source, boolean overlap, String... fragments) {
    return containsAll(source, overlap, Arrays.asList(fragments));
  }

  /**
   * 检查字符串source中是否包含全部指定的子字符串集合fragments，区分大小写
   *
   * @see Strings#containsAll(String, boolean, boolean, Collection)
   */
  public static boolean containsAll(String source, boolean overlap, Collection<String> fragments) {
    return containsAll(source, overlap, true, fragments);
  }

  /**
   * 检查字符串source中是否包含全部指定的子字符串集合fragments
   *
   * @see Strings#containsAll(String, boolean, boolean, Collection)
   */
  public static boolean containsAll(String source, boolean caseSensitive, boolean overlap, String... fragments) {
    return containsAll(source, caseSensitive, overlap, Arrays.asList(fragments));
  }

  /**
   * 检查字符串source中是否包含全部指定的子字符串集合fragments
   * <code>
   * <pre>
   * // return true
   * Strings.containsAll("abcDEF001", true, List.of("ab", "cD", "001"));
   * // return true
   * Strings.containsAll("abcDEF001", true, List.of("ab", "bc", "cD"));
   * // return true
   * Strings.containsAll("abcDEF001", false, true, List.of("ab", "bc", "cd"));
   * // return true
   * Strings.containsAll("abcDEF001", false, false, List.of("ab", "cd", "ef"));
   *
   * // return false
   * Strings.containsAll("abcDEF001", true, List.of("ab", "cd", "001"));
   * // return false
   * Strings.containsAll("abcDEF001", true, true, List.of("ab", "bc", "cd"));
   * // return false
   * Strings.containsAll("abcDEF001", true, false, List.of("ab", "bc", "cD"));
   * </pre>
   * </code>
   *
   * @param fragments     预检查的子字符串
   * @param caseSensitive 是否区分大小写
   * @param overlap       是否允许重叠，默认true。例如source="abc"，parts="ab","bc"，则overlap=true时，返回true（b字符重叠）。overlap=false时，返回false
   */
  public static boolean containsAll(String source,
                                    boolean caseSensitive,
                                    boolean overlap,
                                    Collection<String> fragments) {
    if (isEmpty(source) || fragments == null || fragments.isEmpty()) {
      return true;
    }

    if (overlap) {
      final String aim = caseSensitive ? source : source.toLowerCase();

      return fragments.stream().allMatch(s -> caseSensitive ? aim.contains(s) : aim.contains(s.toLowerCase()));
    }

    String aim = caseSensitive ? source : source.toLowerCase();

    for (String s : fragments) {
      int foundIndex = caseSensitive? aim.indexOf(s) : aim.indexOf(s.toLowerCase());
      //boolean found = caseSensitive ? aim.contains(s) : aim.contains(s.toLowerCase());
      if (foundIndex < 0) {
        return false;
      }

      aim = aim.substring(0, foundIndex) + aim.substring(foundIndex + s.length());
      //aim = remove(aim, List.of(s), 1, caseSensitive);
    }
    return true;
  }

  /**
   * 检查字符串中是否包含任意一个指定的子字符串，区分大小写
   *
   * @see Strings#containsAny(String, boolean, Collection)
   */
  public static boolean containsAny(String source, String... fragments) {
    return containsAny(source, true, Arrays.asList(fragments));
  }

  /**
   * 检查字符串中是否包含任意一个指定的子字符串，区分大小写
   *
   * @see Strings#containsAny(String, boolean, Collection)
   */
  public static boolean containsAny(String source, Collection<String> fragments) {
    return containsAny(source, true, fragments);
  }

  /**
   * 检查字符串中是否包含任意一个指定的子字符串
   *
   * @see Strings#containsAny(String, boolean, Collection)
   */
  public static boolean containsAny(String source, boolean caseSensitive, String... fragments) {
    return containsAny(source, caseSensitive, Arrays.asList(fragments));
  }

  /**
   * 检查字符串中是否包含任意一个指定的子字符串
   * <code>
   * <pre>
   * // return true
   * Strings.containsAny("abcDEF001", List.of("aba", "cD", "002"));
   * // return true
   * Strings.containsAny("abcDEF001", List.of("aba", "cd", "002"), false);
   *
   * // return false
   * Strings.containsAny("abcDEF001", List.of("aba", "cdc", "002"));
   * </pre>
   * </code>
   *
   * @param caseSensitive 是否区分大小写
   * @param fragments     预检查的子字符串
   */
  public static boolean containsAny(String source, boolean caseSensitive, Collection<String> fragments) {
    if (fragments == null || fragments.isEmpty()) {
      return true;
    }

    final String aim = caseSensitive ? source : source.toLowerCase();

    return fragments.stream().anyMatch(s -> caseSensitive ? aim.contains(s) : aim.contains(s.toLowerCase()));
  }

  /**
   * 检查字符串集合fragments中是否全部都包含指定的字符串source，区分大小写
   *
   * @see Strings#allContains(String, boolean, Collection)
   */
  public static boolean allContains(String source, String... fragments) {
    return allContains(source, true, Arrays.asList(fragments));
  }

  /**
   * 检查字符串集合fragments中是否全部都包含指定的字符串source，区分大小写
   *
   * @see Strings#allContains(String, boolean, Collection)
   */
  public static boolean allContains(String source, Collection<String> fragments) {
    return allContains(source, true, fragments);
  }

  /**
   * 检查字符串集合fragments中是否全部都包含指定的字符串source
   *
   * @see Strings#allContains(String, boolean, Collection)
   */
  public static boolean allContains(String source, boolean caseSensitive, String... fragments) {
    return allContains(source, caseSensitive, Arrays.asList(fragments));
  }

  /**
   * 检查字符串集合fragments中是否全部都包含指定的字符串source
   * <code>
   * <pre>
   * // return true
   * Strings.allContains("abc", true, List.of("abc123", "00abc11"));
   * // return true
   * Strings.allContains("abc", false, List.of("abc123", "00abc11", "ABC00"));
   * // return false
   * Strings.allContains("abc", true, List.of("abc", "ABC"));
   * // return false
   * Strings.allContains("abc", false, List.of("abc", "ABC", "000"));
   * </pre>
   * </code>
   */
  public static boolean allContains(String source, boolean caseSensitive, Collection<String> fragments) {
    String aim = caseSensitive ? source : source.toLowerCase();
    return fragments.stream().allMatch(s -> {
      if (isEmpty(s)) {
        return false;
      }
      return caseSensitive ? s.contains(aim) : s.toLowerCase().contains(aim);
    });
  }

  /**
   * 检查字符串集合fragments中是否有任何一个元素包含指定的字符串source，区分大小写
   *
   * @see Strings#anyContains(String, boolean, Collection)
   */
  public static boolean anyContains(String source, String... fragments) {
    return anyContains(source, true, Arrays.asList(fragments));
  }

  /**
   * 检查字符串集合fragments中是否有任何一个元素包含指定的字符串source，区分大小写
   *
   * @see Strings#anyContains(String, boolean, Collection)
   */
  public static boolean anyContains(String source, Collection<String> fragments) {
    return anyContains(source, true, fragments);
  }

  /**
   * 检查字符串集合fragments中是否有任何一个元素包含指定的字符串source
   *
   * @see Strings#anyContains(String, boolean, Collection)
   */
  public static boolean anyContains(String source, boolean caseSensitive, String... fragments) {
    return anyContains(source, caseSensitive, Arrays.asList(fragments));
  }

  /**
   * 检查字符串集合fragments中是否有任何一个元素包含指定的字符串source
   * <code>
   * <pre>
   * // return true
   * Strings.anyContains("abc", true, List.of("abc123", "xyz"));
   * // return true
   * Strings.anyContains("abc", false, List.of("ABC1233", "00"));
   * // return false
   * Strings.anyContains("abc", true, List.of("00", "ABC"));
   * // return false
   * Strings.anyContains("abc", false, List.of("00", "11"));
   * </pre>
   * </code>
   */
  public static boolean anyContains(String source, boolean caseSensitive, Collection<String> fragments) {
    String aim = caseSensitive ? source : source.toLowerCase();
    return fragments.stream().anyMatch(s -> {
      if (isEmpty(s)) {
        return false;
      }
      return caseSensitive ? s.contains(aim) : s.toLowerCase().contains(aim);
    });
  }

  /**
   * 判断与任意一个字符串是否相等，区分大小写
   *
   * @see Strings#equalsAny(String, boolean, String...)
   */
  public static boolean equalsAny(String source, String... fragments) {
    return equalsAny(source, true, Arrays.asList(fragments));
  }

  /**
   * 判断与任意一个字符串是否相等，区分大小写
   *
   * @see Strings#equalsAny(String, boolean, String...)
   */
  public static boolean equalsAny(String source, Collection<String> fragments) {
    return equalsAny(source, true, fragments);
  }

  /**
   * 判断与任意一个字符串是否相等
   *
   * @see Strings#equalsAny(String, boolean, String...)
   */
  public static boolean equalsAny(String source, boolean caseSensitive, String... fragments) {
    return equalsAny(source, caseSensitive, Arrays.asList(fragments));
  }

  /**
   * 判断与任意一个字符串是否相等
   * <code>
   * <pre>
   * // return true
   * Strings.equals("1", "1", "2", "3");
   * // return false
   * Strings.equals("1", "2", "3", "4");
   * </pre>
   * </code>
   *
   * @param caseSensitive 是否区分大小写
   * @param fragments     判断相等的字符串目标数组
   * @return fragments中有任一字符串与source相等，返回true
   */
  public static boolean equalsAny(String source, boolean caseSensitive, Collection<String> fragments) {
    String aim = caseSensitive ? source : source.toLowerCase();
    return fragments.stream().anyMatch(s -> caseSensitive ? aim.contains(s) : aim.contains(s.toLowerCase()));
  }

  // ===================================================================================================================

  /**
   * 插入子字符串到指定下标位置
   * <code>
   * <pre>
   * // return "xyz123456"
   * Strings.insert("123456", "xyz", 0);
   * // return "123xyz456"
   * Strings.insert("123456", "xyz", 3);
   * // return "123456xyz"
   * Strings.insert("123456", "xyz", 6);
   *
   * // return "1234xyz56"
   * Strings.insert("123456", ,"xyz" -2);
   * // return "12xyz3456"
   * Strings.insert("123456", ,"xyz" -4);
   * </pre>
   * </code>
   *
   * @param fragment 需插入字符串
   * @param index    插入下标，为负数时，从字符串尾部定位
   */
  public static String insert(String source, String fragment, int index) throws StringException {
    if (isEmpty(fragment)) {
      return source;
    }
    int sourceLen = source.length();
    int fragmentLen = fragment.length();
    if (Math.abs(index) > sourceLen) {
      throw new StringException("插入字符串下标越界：[size={}, index={}]", sourceLen, index);
    }

    int i;
    if (index < 0) {
      i = sourceLen + index;
    } else {
      i = index;
    }
    char[] sourceChars = source.toCharArray();
    char[] fragmentChars = fragment.toCharArray();
    char[] result = new char[sourceLen + fragmentLen];

    System.arraycopy(sourceChars, 0, result, 0, i);
    System.arraycopy(fragmentChars, 0, result, i, fragmentLen);
    System.arraycopy(sourceChars, i, result, i + fragmentLen, sourceLen - i);

    return new String(result);
  }

  /**
   * 删除指定开始和结束下标范围的字符
   * <code>
   * <pre>
   * //return "123456"
   * Strings.remove("123xyz456", 3, 6);
   * //return "123xyz"
   * Strings.remove("123xyz456", 6, 9);
   * </pre>
   * </code>
   *
   * @param start 开始删除字符的下标
   * @param end   结束删除字符的下标
   */
  public static String remove(String source, int start, int end) throws StringException {
    if (isEmpty(source) || start == end) {
      return source;
    }
    if (start < 0 || end > source.length()) {
      throw new StringException("删除字符下标越界：[size={}, start={}, end={}]", source.length(), start, end);
    }
    if (start > end) {
      throw new StringException("删除范围下标start不能大于end");
    }

    return source.substring(0, start) + source.substring(end);
  }

  /**
   * 删除字符串中出现的指定子字符串，可指定删除次数，区分大小写
   * <code>
   * <pre>
   * // return "xyz"
   * Strings.remove("123xyz456", List.of("123", "456"));
   * // return ""
   * Strings.remove("123xyz456", List.of("123", "456", "xyz"));
   * // return "_abc_123xyz456"
   * Strings.remove("123xyz456_abc_123xyz456", List.of("123", "456", "xyz"), 1);
   * // return "123xyz456_abc_"
   * Strings.remove("123xyz456_abc_123xyz456", List.of("123", "456", "xyz"), -1);
   * // return "123xyz456_abc_"
   * Strings.remove("123xyz456_abc_123xyz456", List.of("123", "456", "XYZ"), -1, false);
   * </pre>
   * </code>
   *
   * @param fragments 需要从字符串中删除的子字符串集合
   * @param counts    删除次数。 0 删除全部。 大于0(N) 从下标0开始，查找N次出现的子字符串并删除，如果N大于字符串中实际出现的次数，效果等于0。 小于0 与大于0作用相同，但从字符串的尾部开始查找删除
   */
  public static String remove(String source, Collection<String> fragments, int counts) throws StringException {
    return remove(source, fragments, counts, true);
  }

  /**
   * 删除字符串中出现的指定子字符串，可指定删除次数
   * <code>
   * <pre>
   * // return "xyz"
   * Strings.remove("123xyz456", List.of("123", "456"));
   * // return ""
   * Strings.remove("123xyz456", List.of("123", "456", "xyz"));
   * // return "_abc_123xyz456"
   * Strings.remove("123xyz456_abc_123xyz456", List.of("123", "456", "xyz"), 1);
   * // return "123xyz456_abc_"
   * Strings.remove("123xyz456_abc_123xyz456", List.of("123", "456", "xyz"), -1);
   * // return "123xyz456_abc_"
   * Strings.remove("123xyz456_abc_123xyz456", List.of("123", "456", "XYZ"), -1, false);
   * </pre>
   * </code>
   *
   * @param fragments     需要从字符串中删除的子字符串集合
   * @param counts        删除次数。 0 删除全部。 大于0(N) 从下标0开始，查找N次出现的子字符串并删除，如果N大于字符串中实际出现的次数，效果等于0。 小于0 与大于0作用相同，但从字符串的尾部开始查找删除
   * @param caseSensitive 是否区分大小写
   */
  public static String remove(String source, Collection<String> fragments, int counts, boolean caseSensitive) throws
      StringException {
    if (isEmpty(source) || fragments == null || fragments.isEmpty()) {
      return source;
    }

    String aim = source;
    if (!caseSensitive) {
      aim = aim.toLowerCase();
    }
    if (counts == 0) {
      for (String fragment : fragments) {
        aim = aim.replace(fragment.toLowerCase(), "");
      }
      return aim;
    }

    boolean forward = counts < 0;
    int times = Math.abs(counts);
    for (int i = 0; i < times; i++) {
      for (String fragment : fragments) {
        String f = caseSensitive ? fragment : fragment.toLowerCase();
        int pos = forward ? aim.lastIndexOf(f) : aim.indexOf(f);
        if (pos >= 0) {
          aim = remove(aim, pos, pos + f.length());
        }
      }
    }
    return aim;
  }

  // ===================================================================================================================

  /**
   * 字符串是否以指定的任意一个子字符串开头，区分大小写
   *
   * @see Strings#startsWith(String, boolean, Collection)
   */
  public static boolean startsWith(String source, String... prefixes) {
    return startsWith(source, true, Arrays.asList(prefixes));
  }

  /**
   * 字符串是否以指定的任意一个子字符串开头，区分大小写
   *
   * @see Strings#startsWith(String, boolean, Collection)
   */
  public static boolean startsWith(String source, Collection<String> prefixes) {
    return startsWith(source, true, prefixes);
  }

  /**
   * 字符串是否以指定的任意一个子字符串开头
   *
   * @see Strings#startsWith(String, boolean, Collection)
   */
  public static boolean startsWith(String source, boolean caseSensitive, String... prefixes) {
    return startsWith(source, caseSensitive, Arrays.asList(prefixes));
  }

  /**
   * 字符串是否以指定的任意一个子字符串开头
   * <code>
   * <pre>
   * // return true
   * Strings.startsWith("123xyz456", List.of("789", "456", "123"));
   * // return false
   * Strings.startsWith("123xyz456", List.of("abc", "xyz"));
   * // return true
   * Strings.startsWith("abc123xyz", false, List.of("123", "ABC"));
   * </pre>
   * </code>
   *
   * @param caseSensitive 是否区分大小写
   * @param prefixes      子字符串
   * @return 是否有一个子字符串是source的前缀
   */
  public static boolean startsWith(String source, boolean caseSensitive, Collection<String> prefixes) {
    String aim = caseSensitive ? source : source.toLowerCase();
    return prefixes.stream().anyMatch(s -> caseSensitive ? aim.startsWith(s) : aim.startsWith(s.toLowerCase()));
  }

  /**
   * 字符串是否以指定的任意一个子字符串结尾，区分大小写
   *
   * @see Strings#endsWith(String, boolean, Collection)
   */
  public static boolean endsWith(String source, String... postfixes) {
    return endsWith(source, true, Arrays.asList(postfixes));
  }

  /**
   * 字符串是否以指定的任意一个子字符串结尾，区分大小写
   *
   * @see Strings#endsWith(String, boolean, Collection)
   */
  public static boolean endsWith(String source, Collection<String> postfixes) {
    return endsWith(source, true, postfixes);
  }

  /**
   * 字符串是否以指定的任意一个子字符串结尾
   *
   * @see Strings#endsWith(String, boolean, Collection)
   */
  public static boolean endsWith(String source, boolean caseSensitive, String... postfixes) {
    return endsWith(source, caseSensitive, Arrays.asList(postfixes));
  }

  /**
   * 字符串是否以指定的任意一个子字符串结尾
   * <code>
   * <pre>
   * // return true
   * Strings.endsWith("123xyz456", List.of("789", "456", "123"));
   * // return false
   * Strings.endsWith("123xyz456", List.of("abc", "xyz"));
   * // return true
   * Strings.endsWith("abc123xyz", false, List.of("123", "XYZ"));
   * </pre>
   * </code>
   *
   * @param caseSensitive 是否区分大小写
   * @param postfixes     子字符串
   * @return 是否有一个子字符串是source的前缀
   */
  public static boolean endsWith(String source, boolean caseSensitive, Collection<String> postfixes) {
    String aim = caseSensitive ? source : source.toLowerCase();
    return postfixes.stream().anyMatch(s -> caseSensitive ? aim.endsWith(s) : aim.endsWith(s.toLowerCase()));
  }

  /**
   * 确保字符串以给定的前缀开始，区分大小写
   *
   * @see Strings#insureStartsWith(String, String, boolean)
   */
  public static String insureStartsWith(String source, String prefix) {
    return insureStartsWith(source, prefix, true);
  }

  /**
   * 确保字符串以给定的前缀开始
   * <code>
   * <pre>
   * // return "abcxyz"
   * Strings.insureStartsWith("xyz", "abc", true);
   * // return "abcABCxyz"
   * Strings.insureStartsWith("ABCxyz", "abc", true);
   * // return "ABCxyz"
   * Strings.insureStartsWith("ABCxyz", "abc", false);
   * </pre>
   * </code>
   *
   * @param prefix 前缀
   */
  public static String insureStartsWith(String source, String prefix, boolean caseSensitive) {
    String aim = caseSensitive ? source : source.toLowerCase();
    boolean exists = caseSensitive ? aim.startsWith(prefix) : aim.startsWith(prefix.toLowerCase());
    if (exists) {
      return source;
    }
    return prefix + aim;
  }

  /**
   * 确保字符串以给定的后缀结束，区分大小写
   *
   * @see Strings#insureEndsWith(String, String, boolean)
   */
  public static String insureEndsWith(String source, String suffix) {
    return insureEndsWith(source, suffix, true);
  }

  /**
   * 确保字符串以给定的后缀结束
   * <code>
   * <pre>
   * // return "abcxyz"
   * Strings.insureEndsWith("abc", "xyz", true);
   * // return "abcXYZxyz"
   * Strings.insureEndsWith("abcXYZ", "xyz", true);
   * // return "abcXYZ"
   * Strings.insureEndsWith("abcXYZ", "xyz", false);
   * </pre>
   * </code>
   *
   * @param suffix 前缀
   */
  public static String insureEndsWith(String source, String suffix, boolean caseSensitive) {
    String aim = caseSensitive ? source : source.toLowerCase();
    boolean exists = caseSensitive ? aim.endsWith(suffix) : aim.endsWith(suffix.toLowerCase());
    if (exists) {
      return source;
    }
    return aim + suffix;
  }

  // ===================================================================================================================

  /**
   * 获取字符串指定长度的缩略，尾部跟填充物
   * <code>
   * <pre>
   * // return "Install the plugi...";
   * TextHelper.brief("Install the plugin; Restart Eclipse and go to Window", 20, "...");
   * // return "默认逻辑是当表单验证失败时,把按钮...";
   * TextHelper.brief("默认逻辑是当表单验证失败时,把按钮给变灰色", 20, "...");
   * </pre>
   * </code>
   *
   * @param length 处理后字符串长度
   * @param filler 填充物
   */
  public static String brief(String source, int length, String filler) {
    if (isEmpty(filler)) {
      return source.substring(0, length);
    } else {
      return source.substring(0, length - filler.length()) + filler;
    }
  }

  /**
   * 使用给定的前缀与后缀包裹指定的子字符串
   * <code>
   * <pre>
   * // return "abc[123]xyz[123]"
   * Strings.wrap("abc123xyz123", "123", "[", "]");
   * </pre>
   * </code>
   *
   * @param fragment 需要处理的子字符串
   * @param prefix   前缀
   * @param suffix   后缀
   */
  public static String wrap(String source, String fragment, String prefix, String suffix) {
    if (isEmpty(source)) {
      return source;
    }

    return source.replace(fragment, prefix + fragment + suffix);
  }

  /**
   * 将字符串反转
   * <code>
   * <pre>
   * // return "321"
   * Strings.reverse("123");
   * // return "zyx_cba"
   * Strings.reverse("abc_xyz");
   * </pre>
   * </code>
   */
  public static String reverse(String source) {
    if (isEmpty(source)) {
      return source;
    }
    return new StringBuilder(source).reverse().toString();
  }

  /**
   * 获取字符串最后的部分，直到出现参数fragment为止
   * <code>
   * <pre>
   * // return "core"
   * Strings.lastUntil("/root/dean/core", "/");
   * // return "/core"
   * Strings.lastUntil("/root/dean/core", "/", true);
   * // return "/root/dean/core"
   * Strings.lastUntil("/root/dean/core", "=");
   * </pre>
   * </code>
   *
   * @param fragment 截取尾部字符串的标识字符
   * @param extend   为true时，结果字符串开头带有fragment
   * @return 尾部的子字符串，如没有出现fragment，返回原字符串
   */
  public static String lastUntil(String source, String fragment, boolean extend) {
    int i = source.lastIndexOf(fragment);
    if (i < 0) {
      return source;
    } else if (extend) {
      return source.substring(i);
    } else {
      return source.substring(i + fragment.length());
    }
  }

  /**
   * 获取字符串开始的部分，直到出现参数fragment为止
   * <code>
   * <pre>
   * // return "C:"
   * Strings.frontUntil("C:/windows/system32", "/");
   * // return "C:/"
   * Strings.frontUntil("C:/windows/system32", "/", true);
   * // return "C:/windows/system32"
   * Strings.frontUntil("C:/windows/system32", "=");
   * </pre>
   * </code>
   *
   * @param fragment 截取头部字符串的标识字符
   * @param extend   为true时，结果字符串结尾带有fragment
   * @return 开头的子字符串，如没有出现fragment，返回原字符串
   */
  public static String frontUntil(String source, String fragment, boolean extend) {
    int i = source.indexOf(fragment);
    if (i < 0) {
      return source;
    } else if (i == 0 && extend) {
      return fragment;
    } else if (i == 0) {
      return "";
    } else if (extend) {
      return source.substring(0, i + fragment.length());
    } else {
      return source.substring(0, i);
    }
  }

  public enum Position {
    LEFT, RIGHT, BOTH
  }

  public enum Cases {
    /**
     * 小写
     */
    LOWERCASE,
    /**
     * 大写
     */
    UPPERCASE,
    /**
     * 保持不变
     */
    KEEP
  }
}
