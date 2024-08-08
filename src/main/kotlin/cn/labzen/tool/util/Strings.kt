package cn.labzen.tool.util

import cn.labzen.tool.bean.Pair
import cn.labzen.tool.exception.StringException
import cn.labzen.tool.kotlin.throwRuntimeIf
import com.google.common.base.Ascii
import kotlin.math.abs

object Strings {

  /**
   * 如果[source]为null，返回[default]。否则返回[source]
   * @param source String? 任意对象
   * @param default String? 当[source]为null时返回的值
   * @return String? [source]或第二个参数
   */
  @JvmStatic
  fun value(source: String?, default: String?): String? = source ?: default

  /**
   * 如果[source]为null，返回[default]。否则返回[source].toString()
   */
  @JvmStatic
  fun from(source: Any?, default: String?): String? = source?.toString() ?: default

  /**
   * 如果参数[source]字符串值，匹配[pattern]，则替换为[replacement]，否则返回[source]
   * @param source String? 任意对象
   * @param pattern String 匹配正则表达式
   * @param replacement String 替代字符串
   * @return String? 匹配[pattern]的字符串替换为[replacement]，否则原样返回
   */
  @JvmStatic
  fun value(source: String?, pattern: String, replacement: String): String? =
    if (source?.matches(Regex(pattern)) == true) replacement else source

  // ===================================================================================================================

  /**
   * 判断字符串是否为null或empty
   * @param source String? 任意字符串
   * @return Boolean 是null或empty
   */
  @JvmStatic
  fun isEmpty(source: String?) = source.isNullOrEmpty()

  /**
   * 判断字符串是否为null或blank
   * @param source String? 任意字符串
   * @return Boolean 是null或blank
   */
  @JvmStatic
  fun isBlank(source: String?) = source.isNullOrBlank()

  /**
   * 判断字符串是否为null或empty
   * @param source String? 任意字符串
   * @return Boolean 是null或empty
   */
  @JvmStatic
  fun isNotEmpty(source: String?) = !isEmpty(source)

  /**
   * 判断字符串是否为null或blank
   * @param source String? 任意字符串
   * @return Boolean 是null或blank
   */
  @JvmStatic
  fun isNotBlank(source: String?) = !isBlank(source)

  /**
   * 检查给出的字符串集合中，是否任意一个为null或空字符
   * ```
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
   * ```
   */
  @JvmStatic
  fun isAnyBlank(vararg sources: String?): Boolean =
    sources.any { it?.isBlank() ?: true }

  /**
   * @see [isAnyBlank]
   */
  @JvmStatic
  fun isAnyBlank(sources: List<String?>): Boolean =
    sources.any { it?.isBlank() ?: true }

  /**
   * 检查给出的字符串集合中，是否任意一个为null或空字符
   * ```
   * // return true
   * Strings.isAllBlank("string", null, "", "  ");
   * // return true
   * Strings.isAllBlank("string", "notNull", "  ");
   * // return true
   * Strings.isAllBlank("string", "notNull", "");
   * // return true
   * Strings.isAllBlank("string", "notNull", null);
   * // return false
   * Strings.isAllBlank("string", "notNull", "notBlank");
   * ```
   */
  @JvmStatic
  fun isAllBlank(vararg sources: String?): Boolean =
    sources.all { it?.isBlank() ?: true }

  /**
   * @see [isAllBlank]
   */
  @JvmStatic
  fun isAllBlank(sources: List<String?>): Boolean =
    sources.all { it?.isBlank() ?: true }

  /**
   * 如果字符串为empty（null或空字符串""），返回null
   * @param source String? 任意字符串
   * @return String? 如果字符串为empty，返回null，否则原样返回
   */
  @JvmStatic
  fun emptyToNull(source: String?): String? = if (isEmpty(source)) null else source

  /**
   * 如果字符串为blank（null或空字符串“”或`空白`字符串"  "），返回null
   * @param source String? 任意字符串
   * @return String? 如果字符串为blank，返回null，否则原样返回
   */
  @JvmStatic
  fun blankToNull(source: String?): String? = if (isBlank(source)) null else source

  /**
   * 如果字符串为empty（null或空字符串""），返回参数[replacement]
   * @param source String? 任意字符串
   * @param replacement String 替代字符串
   * @return String? 如果字符串为empty，返回replacement，否则原样返回
   */
  @JvmStatic
  fun emptyTo(source: String?, replacement: String) = if (isEmpty(source)) replacement else source

  /**
   * 如果字符串为blank（null或空字符串“”或空白字符串" "），返回参数[replacement]
   * @param source String? 任意字符串
   * @param replacement String 替代字符串
   * @return String? 如果字符串为blank，返回replacement，否则原样返回
   */
  @JvmStatic
  fun blankTo(source: String?, replacement: String) = if (isBlank(source)) replacement else source

  /**
   * 如果字符串为null，返回参数[replacement]
   * @param source String? 任意字符串
   * @param replacement String 替代字符串
   * @return String? 如果字符串为null，返回replacement，否则原样返回
   */
  @JvmStatic
  fun nullTo(source: String?, replacement: String) = source ?: replacement

  // ===================================================================================================================

  /**
   * 连接多个字符串，null值将被忽略
   * ```
   * // return "123";
   * Strings.concat("1", "", "2", null, "3");
   * ```
   * @param parts Array<out String?> 字符串数组
   * @return String 连接后的字符串
   */
  @JvmStatic
  fun concat(vararg parts: String?): String =
    parts.filterNotNull().joinToString("")

  /**
   * 拼接多个字符串
   * ```
   * List strings = new ArrayList();
   * strings.add("1");
   * strings.add("");
   * strings.add("2");
   * strings.add(null);
   * strings.add("3");
   * // return "123";
   * Strings.concat(strings);
   * ```
   * @param parts Iterable<String?> 字符串数组
   * @return String 连接后的字符串
   */
  @JvmStatic
  fun concat(parts: Iterable<String?>): String = parts.filterNotNull().joinToString("")

  /**
   * 拼接多个字符串，之间用separator间隔，null值将被忽略
   * ```
   * // return "1::2:3";
   * Strings.concat(":", "1", "", "2", null, "3");
   * ```
   * @param separator String 分隔符
   * @param parts Array<out String?> 字符串数组
   * @return String 连接后的字符串
   */
  @JvmStatic
  fun join(separator: String, vararg parts: String?): String =
    join(separator, parts.toList())

  /**
   * 拼接多个字符串，之间用separator间隔，null值将被忽略
   * ```
   * List strings = new ArrayList();
   * strings.add("1");
   * strings.add("");
   * strings.add("2");
   * strings.add(null);
   * strings.add("3");
   * // return "1::2:3";
   * Strings.join(":", strings);
   * ```
   * @param separator String 分隔符
   * @param parts Iterable<String> 字符串数组
   * @return String 连接后的字符串
   */
  @JvmStatic
  fun join(separator: String, parts: Iterable<String?>): String =
    parts.filterNotNull().joinToString(separator)

  // ===================================================================================================================

  /**
   * 对source修剪指定的字符串redundant
   * ```
   * // return "123"
   * Strings.trim("===123===", "=", 0);
   * // return "123==="
   * Strings.trim("===123===", "=", 1);
   * // return "===123"
   * Strings.trim("===123===", "=", -1);
   * ```
   * @param source String 字符串
   * @param redundant String 需被修剪的字符串
   * @param place Int 修剪方向，0修剪两侧，负数修剪右侧，正数修剪左侧，默认0
   * @return String
   */
  @JvmStatic
  @JvmOverloads
  fun trim(source: String, redundant: String, place: Int = 0): String {
    if (redundant === "") return source

    val length = source.length
    val step = redundant.length
    var start = 0
    var end = length

    if (place >= 0) {
      while (start < end && source.indexOf(redundant, start) == start) {
        start += step
      }
    }

    if (place <= 0) {
      while (start < end && source.lastIndexOf(redundant, end - 1) == (end - step)) {
        end -= step
      }
    }

    return if (start > 0 || end < length)
      source.substring(start, end)
    else
      source
  }

  // ===================================================================================================================

  /**
   * 获取指定下标的单个字母字符串，index为负数时，从后向前找，index从0开始计数，0返回左侧第一个字符
   * ```
   * // return "4";
   * Strings.at("123456789", 3);
   * // return "6";
   * Strings.at("123456789", -4);
   * ```
   * @param source String 源字符串
   * @param index Int 下标
   * @return String 长度为1的字符串，未找到返回空
   */
  @JvmStatic
  fun at(source: String, index: Int): Char? {
    val size = source.length
    val i = if (index < 0) size + index else index
    return if (i in 0 until size) source[i] else null
  }

  /**
   * 截取从指定下标开始之前/之后的子字符串。index为截取的开始下标，为负数时，下标从源字符串最后向前确定。
   * length决定子字符串的长度，当length为负数时，代表从后向前截取
   * ```
   * // return "345"
   * Strings.sub("0123456789", 3, 3);
   * // return "123"
   * Strings.sub("0123456789", 3, -3);
   * // return "789"
   * Strings.sub("0123456789", -3, 3);
   * // return "567"
   * Strings.sub("0123456789", -3, -3);
   *
   * // return "56789"
   * Strings.sub("0123456789", 5, 8);
   * // return "012345"
   * Strings.sub("0123456789", -5, -8);
   * ```
   * @param source String 源字符串
   * @param start Int 开始下标
   * @param length Int 截取长度及方向
   * @return String 子字符串
   * @throws StringException 如果截取子字符串的长度超过可截取范围，则抛出异常
   */
  @JvmStatic
  fun sub(source: String, start: Int, length: Int): String {
    val size = source.length
    if (length == 0 || size == 0) {
      return ""
    }
    (abs(length) > size).throwRuntimeIf { StringException("截取子字符串长度大于源字符串") }

    val forward = length < 0
    val po1 = if (start < 0) size + start else start
    val po2 = po1 + length

    (forward && po2 < 0).throwRuntimeIf { StringException("字符串截取开始下标越界：[size=$size, start=$po2, end=$po1]") }
    (!forward && po2 > size).throwRuntimeIf { StringException("字符串截取结束下标越界：[size=$size, start=$po1, end=$po2]") }

    return if (forward) source.substring(po2 + 1, po1 + 1) else source.substring(po1, po2)
  }

  /**
   * 获取指定的字符区间的内容
   * ```
   * // return Lists.newArrayList("abc", "def")
   * Strings.between("[abc] xyz [def]", "[", "]");
   * ```
   * @param source String 字符串
   * @param start Char 开始字符串，不能与end相同
   * @param end Char 结束字符串，不能与start相同
   * @return List<String>
   */
  @JvmStatic
  fun between(source: String, start: Char, end: Char): List<String> {
    val ret = mutableListOf<String>()
    var tmp: StringBuilder? = null
    var foundOne = false
    source.toCharArray().forEach {
      when {
        !foundOne && it == start -> {
          tmp = StringBuilder()
          foundOne = true
        }

        foundOne && it == end -> {
          ret.add(tmp.toString())
          tmp = null
          foundOne = false
        }

        else -> {
          tmp?.append(it)
          tmp?.run {
          }
        }
      }
    }

    return ret.toList()
  }

  /**
   * 按分隔符切分字符串为两部分
   * ```
   * // return Pair("a", "b")
   * Strings.cut("a-b", "-")
   * // return Pair("localhost", "8080")
   * Strings.cut("localhost:8080", ":")
   * ```
   * @param source String 字符串
   * @param separator String 分隔符，必须唯一，否则返回null
   * @param caseSensitive Boolean 区分大小写，默认true
   * @return Pair<String, String> 分隔符两遍的字符串
   */
  @JvmStatic
  @JvmOverloads
  fun cut(source: String, separator: String, caseSensitive: Boolean = true): Pair<String, String>? =
    if (times(source, separator, caseSensitive) == 1) {
      source.split(separator).let { Pair(it[0], it[1]) }
    } else null

  // ===================================================================================================================

  private const val DELIMITER_START = '{'
  private const val DELIMITER_STR = "{}"
  private const val ESCAPE_CHAR = '\\'

  private fun formatArguments(buf: StringBuilder, o: Any?) {
    buf.append(o?.toString() ?: "[null]")
  }

  /**
   * 格式化字符串，使用可变数量参数代替掉字符串中出现的{}
   * ```
   * // return "a=1,b=2,a+b=3"
   * Strings.format("a={},b={},a+b={}", Lists.newArrayList("1", "2", "3"))
   * // return "a=1,b=2,a+b=\\{3}"
   * Strings.format("a={},b={},a+b=\\{{}}", Lists.newArrayList("1", "2", "3"))
   * // return "a=1,b=2,a+b={}3"
   * Strings.format("a={},b={},a+b=\\{}{}", Lists.newArrayList("1", "2", "3"))
   * // return "a=1,b=2,a+b=\\3"
   * Strings.format("a={},b={},a+b=\\\\{}", Lists.newArrayList("1", "2", "3"))
   * // return "a={},b={},a+b={}"
   * Strings.format("a={},b={},a+b={}")
   * // return "a=1,b=2,a+b=3"
   * Strings.format("a={},b={},a+b={}", Lists.newArrayList("1", "2", "3", "4"))
   * // return "a=1,b=2,a+b={}"
   * Strings.format("a={},b={},a+b={}", Lists.newArrayList("1", "2"))
   * ```
   * @param pattern String 格式化字符串
   * @param arguments Array<out Any> 参数
   * @return String 格式化后的字符串
   */
  @JvmStatic
  fun format(pattern: String, arguments: Iterable<Any?>): String {
    var start = 0
    var cur: Int
    var ai = 0
    val buf = StringBuilder()

    val args = arguments.toList()

    while (ai < args.size) {
      val arg = args[ai]?.toString()
      cur = pattern.indexOf(DELIMITER_STR, start)

      if (cur == -1) break

      var escaped = false
      if (cur > 0) {
        val c = at(pattern, cur - 1)
        escaped = c == ESCAPE_CHAR
      }
      if (escaped) {
        val c = at(pattern, cur - 2)
        start = if (c == ESCAPE_CHAR) {
          buf.append(pattern, start, cur - 1)
          formatArguments(buf, arg)
          cur + 2
        } else {
          ai--
          buf.append(pattern, start, cur - 1)
          buf.append(DELIMITER_START)
          cur + 1
        }
      } else {
        buf.append(pattern, start, cur)
        formatArguments(buf, arg)
        start = cur + 2
      }

      ai++
    }

    buf.append(pattern, start, pattern.length)
    return buf.toString()
  }

  /**
   * 格式化字符串，使用可变数量参数代替掉字符串中出现的{}
   * ```
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
   * ```
   * @param pattern String 格式化字符串
   * @param arguments Array<out Any> 参数
   * @return String 格式化后的字符串
   */
  @JvmStatic
  fun format(pattern: String, vararg arguments: Any?): String = format(pattern, arguments.asList())

  // ===================================================================================================================

  /**
   * 统计looking4子字符串在source字符串中出现的次数。从source中查找子字符串出现的开始位置，
   * 是在上一次找到的位置下标 + 子字符串的长度。也即是说查找"aaa"中"aa"出现的次数，这里是1次，而不是2次
   * ```
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
   * ```
   * @param source String 字符串
   * @param looking4 String 要统计的子字符串
   * @param caseSensitive Boolean 区分大小写，默认true
   * @return Int 在source中出现的次数
   */
  @JvmStatic
  @JvmOverloads
  fun times(source: String, looking4: String, caseSensitive: Boolean = true): Int {
    if (looking4 === "") return 0

    var times = 0
    var position = 0
    while (true) {
      position = source.indexOf(looking4, position, !caseSensitive)
      if (position == -1) {
        break
      }
      position += looking4.length
      times++
    }

    return times
  }

  // ===================================================================================================================

  /**
   * 清除字符串中单词间多余的空格（单词间只保留一个空格），以及两侧的空格
   * ```
   * // return "sample english sentence."
   * Strings.simplify("  sample   english   sentence.   ")
   * ```
   * @param source String
   * @return String
   */
  @JvmStatic
  fun simplify(source: String): String = source.replace(Regex("\\s+"), " ").trim()

  // ===================================================================================================================

  private val CASE_SEPARATOR = Regex("[_\\- ]")

  private fun case(source: String, start: Int, length: Int, block: Char.() -> Char): String {
    val size = source.length
    if (length < 0 || size == 0) return source

    (abs(length) > size).throwRuntimeIf { StringException("需要处理的长度大于源字符串") }

    val po1 = if (start < 0) size + start else start
    val po2 = if (length == 0) size else po1 + length

    (po2 > size).throwRuntimeIf { StringException("转换处理结束下标越界：[size=$size, start=$po1, end=$po2]") }

    val chars = source.toCharArray()
    for (i: Int in po1 until po2) {
      chars[i] = block(chars[i])
    }
    return String(chars)
  }

  /**
   * 小写指定字符
   * ```
   * // return "abc"
   * Strings.toLowerCase("ABC", 0);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1, 1);
   * // return "Abc"
   * Strings.toLowerCase("ABC", 1, 2);
  ```
   * @param source String 字符串
   * @param start Int 需要小写的字符（开始）下标，为负数从字符串尾部偏移下标
   * @param length Int 需要处理的长度，不能为负数，设为0（默认），处理至字符串尾部
   * @return String 部分字符小写后的字符串
   * @throws StringException
   */
  @JvmStatic
  @JvmOverloads
  fun toLowerCase(source: String, start: Int, length: Int = 0) =
    case(source, start, length) {
      Ascii.toLowerCase(this)
    }

  /**
   * 大写指定字符
   * ```
   * // return "ABC"
   * Strings.toUpperCase("abc", 0);
   * // return "aBC"
   * Strings.toUpperCase("abc", 1);
   * // return "aBc"
   * Strings.toUpperCase("abc", 1, 1);
   * // return "aBC"
   * Strings.toUpperCase("abc", 1, 2);
   * ```
   * @param source String 字符串
   * @param start Int 需要大写的字符（开始）下标，为负数从字符串尾部偏移下标
   * @param length Int 需要处理的长度，不能为负数，设为0（默认），处理至字符串尾部
   * @return String 部分字符大写后的字符串
   */
  @JvmStatic
  @JvmOverloads
  fun toUpperCase(source: String, start: Int, length: Int = 0) =
    case(source, start, length) {
      Ascii.toUpperCase(this)
    }

  private val sectionSpaceCharacters = arrayOf(' ', '-', '_', '\r', '\n', '\t')
  private fun slicingStrings(source: String, toLowercase: Boolean? = null): List<String> {
    if (source.isEmpty()) return emptyList()

    val result = mutableListOf<String>()
    val currentString = StringBuilder()

    var i = 0
    while (i < source.length) {
      val char = source[i]
      if (sectionSpaceCharacters.contains(char)) {
        if (currentString.isNotEmpty()) {
          result.add(
            when (toLowercase) {
              null -> currentString.toString()
              true -> currentString.toString().lowercase()
              false -> currentString.toString().uppercase()
            }
          )
          currentString.clear()
        }

        // 跳过连续的特殊字符
        while (i < source.length && sectionSpaceCharacters.contains(source[i])) {
          i++
        }
        continue
      }

      if (char.isUpperCase() && currentString.isNotEmpty()) {
        result.add(
          when (toLowercase) {
            null -> currentString.toString()
            true -> currentString.toString().lowercase()
            false -> currentString.toString().uppercase()
          }
        )
        currentString.clear()
      }

      currentString.append(char)
      i++
    }

    // 添加最后一个
    if (currentString.isNotEmpty()) {
      result.add(
        when (toLowercase) {
          null -> currentString.toString()
          true -> currentString.toString().lowercase()
          false -> currentString.toString().uppercase()
        }
      )
    }

    return result
  }

  /**
   * 转换成驼峰式，首字母大写
   * ```
   * // return "ThereIsAWord"
   * Strings.studlyCase("  there is a word"));
   * // return "ThereIsAWord"
   * Strings.studlyCase("there_is_a_word  "));
   * // return "ThereIsAWord"
   * Strings.studlyCase(" there-is-a-word "));
   * // return ""
   * Strings.studlyCase("   "));
   * ```
   * @param source String 字符串
   * @return String 大写驼峰命名
   */
  @JvmStatic
  fun studlyCase(source: String): String =
    slicingStrings(source, true).joinToString("") { w -> toUpperCase(w, 0, 1) }

  /**
   * 转换成驼峰式，首字母小写
   * ```
   * // return "thereIsAWord"
   * Strings.camelCase("  there is a word"));
   * // return "thereIsAWord"
   * Strings.camelCase("there_is_a_word  "));
   * // return "thereIsAWord"
   * Strings.camelCase(" there-is-a-word "));
   * // return ""
   * Strings.camelCase("   "));
   * ```
   * @param source String 字符串
   * @return String 小写驼峰命名
   */
  @JvmStatic
  fun camelCase(source: String): String =
    toLowerCase(studlyCase(source), 0, 1)

  /**
   * 转换成Snake Case，以下划线间隔
   * ```
   * //"there_is_a_word"
   * Strings.snakeCase("  there is a word");
   * //"there_is_a_word"
   * Strings.snakeCase("there_is_a_word  ");
   * //"there_is_a_word"
   * Strings.snakeCase(" there-is-a-word ");
   * //""
   * Strings.snakeCase("   ");
   * ```
   * @param source String 字符串
   * @param toLowercase 是否将所有字符转换为小写，默认null；null - 保持原字符，true - 转小写, false - 转大写
   * @return String 蛇形命名
   */
  @JvmStatic
  @JvmOverloads
  fun snakeCase(source: String, toLowercase: Boolean? = null): String =
    slicingStrings(source, toLowercase).joinToString("_")

  /**
   * 转换成Kebab Case，以短横线间隔
   * ```
   * // return "there-is-a-word"
   * Strings.kebabCase("  there is a word");
   * // return "there-is-a-word"
   * Strings.kebabCase("there_is_a_word  ");
   * // return "there-is-a-word"
   * Strings.kebabCase(" there-is-a-word ");
   * // return ""
   * Strings.kebabCase("   ");
   * ```
   * @param source String 字符串
   * @param toLowercase 是否将所有字符转换为小写，默认null；null - 保持原字符，true - 转小写, false - 转大写
   * @return String 短横线命名
   */
  @JvmStatic
  @JvmOverloads
  fun kebabCase(source: String, toLowercase: Boolean? = null): String =
    slicingStrings(source, toLowercase).joinToString("-")

  // ===================================================================================================================

  /**
   * 重复给定的字符串直到字符串达到或超过给定长度
   * ```
   * // return "00000"
   * Strings.repeatUntil("0", 5);
   * // return "01010"
   * Strings.repeatUntil("01", 5);
   * // return "01201"
   * Strings.repeatUntil("012", 5);
   * ```
   * @param source String 字符串
   * @param length Int 最终长度
   * @return String 处理后的字符串
   * @throws StringException
   */
  @JvmStatic
  fun repeatUntil(source: String, length: Int): String {
    (length < 0).throwRuntimeIf { StringException("重复的长度不能为负数") }

    if (length == 0 || source.isEmpty()) return ""

    val size = source.length
    val array = CharArray(length)
    source.toCharArray(array, 0, 0, size)

    var n = size
    while (n < length - n) {
      System.arraycopy(array, 0, array, n, n)
      n = n shl 1
    }
    System.arraycopy(array, 0, array, n, length - n)

    return String(array)
  }

  /**
   * 补充字符串达到给定长度， length如果为负数，将字符串填充至尾部。类似Guava的Strings.padStart()或Strings.padEnd()
   * ```
   * // return "binary numbers look like ..."
   * Strings.fill("binary numbers look like ", ".", -28);
   * // return "binary numbers look like 01010"
   * Strings.fill("binary numbers look like ", "01", -30);
   * // return "!@#!@#!binary numbers look like "
   * Strings.fill("binary numbers look like ", "!@#", 32);
   * ```
   * @param source String 字符串
   * @param filler String 补充的字符串
   * @param length Int 最终长度
   * @return String 处理后的字符串
   */
  @JvmStatic
  fun fill(source: String, filler: String, length: Int): String {
    val len = abs(length)
    if (len <= source.length || filler.isEmpty()) return source

    val tail = length < 0
    val pad = repeatUntil(filler, len - source.length)
    return if (tail) source + pad else pad + source
  }

  // ===================================================================================================================

  /**
   * 检查字符串中是否包含全部指定的子字符串
   * ```
   * // return true
   * Strings.haveAll("abcDEF001", Lists.newArrayList("ab", "cD", "001"));
   * // return true
   * Strings.haveAll("abcDEF001", Lists.newArrayList("ab", "bc", "cD"));
   * // return true
   * Strings.haveAll("abcDEF001", Lists.newArrayList("ab", "bc", "cd"), false, true);
   * // return true
   * Strings.haveAll("abcDEF001", Lists.newArrayList("ab", "cd", "ef"), false, false);
   *
   * // return false
   * Strings.haveAll("abcDEF001", Lists.newArrayList("ab", "cd", "001"));
   * // return false
   * Strings.haveAll("abcDEF001", Lists.newArrayList("ab", "bc", "cd"), true, true);
   * // return false
   * Strings.haveAll("abcDEF001", Lists.newArrayList("ab", "bc", "cD"), true, false);
   * ```
   * @param source String 字符串
   * @param looking4 Collection<String> 预检查的子字符串
   * @param caseSensitive Boolean 区分大小写，默认true
   * @param overlap Boolean 允许重叠，默认true。例如source="abc"，parts=["ab","bc"]，则overlap=true时，返回true（b字符重叠）。overlap=false时，返回false
   * @return Boolean looking4中的字符串全部包含在source中
   */
  @JvmStatic
  @JvmOverloads
  fun haveAll(
    source: String,
    looking4: Collection<String>,
    caseSensitive: Boolean = true,
    overlap: Boolean = true
  ): Boolean {
    if (source.isEmpty() || looking4.isEmpty()) return true

    return if (overlap) {
      looking4.all { source.contains(it, !caseSensitive) }
    } else {
      var copy = source

      for (s in looking4) {
        val h = copy.contains(s, !caseSensitive)
        if (!h) return false

        copy = remove(copy, listOf(s), caseSensitive = caseSensitive)
      }
      return true
    }
  }

  /**
   * 检查字符串中是否包含任意一个指定的子字符串
   * ```
   * // return true
   * Strings.haveAny("abcDEF001", Lists.newArrayList("aba", "cD", "002"));
   * // return true
   * Strings.haveAny("abcDEF001", Lists.newArrayList("aba", "cd", "002"), false);
   *
   * // return false
   * Strings.haveAny("abcDEF001", Lists.newArrayList("aba", "cdc", "002"));
   * ```
   * @param source String 字符串
   * @param looking4 List<String> 预检查的子字符串
   * @param caseSensitive Boolean 区分大小写，默认true
   * @return Boolean looking4中的字符串，最少有一个包含在source中
   */
  @JvmStatic
  @JvmOverloads
  fun haveAny(source: String, looking4: List<String>, caseSensitive: Boolean = true): Boolean {
    if (looking4.isEmpty()) return true
    return looking4.any { source.contains(it, !caseSensitive) }
  }

  // ===================================================================================================================

  /**
   * 插入子字符串到指定下标位置
   * ```
   * // return "xyz123456"
   * Strings.insert("123456", 0,"xyz");
   * // return "123xyz456"
   * Strings.insert("123456", 3,"xyz");
   * // return "123456xyz"
   * Strings.insert("123456", 6,"xyz");
   *
   * // return "1234xyz56"
   * Strings.insert("123456", -2,"xyz");
   * // return "12xyz3456"
   * Strings.insert("123456", -4,"xyz");
   * ```
   * @param source String 字符串
   * @param index Int 插入下标，为负数时，从字符串尾部定位
   * @param part String 需插入字符串
   * @return String 插入后的字符串
   */
  @JvmStatic
  fun insert(source: String, index: Int, part: String): String {
    if (part.isEmpty()) return source
    (abs(index) > source.length).throwRuntimeIf { StringException("插入字符串下标越界：[size=${source.length}, index=$index]") }

    val i = if (index < 0) source.length + index else index

    val cs = source.toCharArray()
    val cp = part.toCharArray()
    val cr = CharArray(source.length + part.length)

    System.arraycopy(cs, 0, cr, 0, i)
    System.arraycopy(cp, 0, cr, i, part.length)
    System.arraycopy(cs, i, cr, i + part.length, source.length - i)

    return String(cr)
  }

  /**
   * 删除指定开始和结束下标范围的字符
   * ```
   * //return "123456"
   * Strings.remove("123xyz456", 3, 6);
   * //return "123xyz"
   * Strings.remove("123xyz456", 6, 9);
   * ```
   * @param source String 字符串
   * @param start Int 开始删除字符的下标
   * @param end Int 结束删除字符的下标
   * @return String 删除指定字符后的字符串
   */
  @JvmStatic
  fun remove(source: String, start: Int, end: Int): String {
    if (source.isEmpty() || start == end) return source
    (start < 0 || end > source.length).throwRuntimeIf { StringException("删除字符下标越界：[size=${source.length}, start=$start, end=$end]") }
    (start > end).throwRuntimeIf { StringException("删除范围下标start不能大于end") }

    return source.removeRange(start, end)
  }

  /**
   * 删除字符串中出现的指定子字符串，可指定删除次数
   * ```
   * // return "xyz"
   * Strings.remove("123xyz456", Lists.newArrayList("123", "456"));
   * // return ""
   * Strings.remove("123xyz456", Lists.newArrayList("123", "456", "xyz"));
   * // return "_abc_123xyz456"
   * Strings.remove("123xyz456_abc_123xyz456", Lists.newArrayList("123", "456", "xyz"), 1);
   * // return "123xyz456_abc_"
   * Strings.remove("123xyz456_abc_123xyz456", Lists.newArrayList("123", "456", "xyz"), -1);
   * // return "123xyz456_abc_"
   * Strings.remove("123xyz456_abc_123xyz456", Lists.newArrayList("123", "456", "XYZ"), -1, false);
   * ```
   * @param source String 字符串
   * @param parts Collection<String> 需要从字符串中删除的子字符串
   * @param counts Int 删除次数，默认0。 0 删除全部。 大于0(N) 从下标0开始，查找N次出现的子字符串并删除，如果N大于字符串中实际出现的次数，效果等于0。 小于0 与大于0作用相同，但从字符串的尾部开始查找删除
   * @param caseSensitive Boolean 区分大小写，默认true
   * @return String 删除后的字符串
   */
  @JvmStatic
  @JvmOverloads
  fun remove(source: String, parts: Collection<String>, counts: Int = 0, caseSensitive: Boolean = true): String {
    if (source.isEmpty() || parts.isEmpty()) return source

    var copy = source
    if (counts == 0) {
      parts.forEach { copy = copy.replace(it, "", !caseSensitive) }
    } else {
      val forward = counts < 0

      for (i in 0 until abs(counts)) {
        parts.forEach {
          val ind = if (forward)
            copy.lastIndexOf(it, ignoreCase = !caseSensitive)
          else
            copy.indexOf(it, ignoreCase = !caseSensitive)
          if (ind >= 0) {
            copy = remove(copy, ind, ind + it.length)
          }
        }
      }
    }
    return copy
  }

  // ===================================================================================================================

  /**
   * 字符串是否以指定的任意一个子字符串开头
   * ```
   * // return true
   * Strings.startsWith("123xyz456", Lists.newArrayList("789", "456", "123"));
   * // return false
   * Strings.startsWith("123xyz456", Lists.newArrayList("abc", "xyz"));
   * // return true
   * Strings.startsWith("abc123xyz", false, Lists.newArrayList("123", "ABC"));
   * ```
   * @param source String 字符串
   * @param prefixes Iterable<String> 子字符串
   * @param caseSensitive Boolean 区分大小写，默认true
   * @return Boolean 是否有一个子字符串是source的前缀
   */
  @JvmStatic
  @JvmOverloads
  fun startsWith(source: String, caseSensitive: Boolean = true, prefixes: Iterable<String>) =
    prefixes.any { source.startsWith(it, !caseSensitive) }

  /**
   * 字符串是否以指定的任意一个子字符串开头
   * ```
   * // return true
   * Strings.startsWith("123xyz456", "789", "456", "123");
   * // return false
   * Strings.startsWith("123xyz456", "abc", "xyz");
   * // return true
   * Strings.startsWith("abc123xyz", false, "123", "ABC");
   * ```
   * @param source String 字符串
   * @param caseSensitive Boolean 区分大小写，默认true
   * @param prefixes Array<out String> 子字符串
   * @return Boolean 是否有一个子字符串是source的前缀
   */
  @JvmStatic
  @JvmOverloads
  fun startsWith(source: String, caseSensitive: Boolean = true, vararg prefixes: String) =
    startsWith(source, caseSensitive, prefixes.toList())

  /**
   * 字符串是否以指定的任意一个子字符串结尾
   * ```
   * // return true
   * Strings.endsWith("123xyz456", Lists.newArrayList("789", "456", "123"));
   * // return false
   * Strings.endsWith("123xyz456", Lists.newArrayList("abc", "xyz"));
   * // return true
   * Strings.endsWith("abc123xyz", false, Lists.newArrayList("123", "XYZ"));
   * ```
   * @param source String 字符串
   * @param caseSensitive Boolean 区分大小写，默认true
   * @param postfixes Iterable<String> 子字符串
   * @return Boolean 是否有一个子字符串是source的后缀
   */
  @JvmStatic
  @JvmOverloads
  fun endsWith(source: String, caseSensitive: Boolean = true, postfixes: Iterable<String>) =
    postfixes.any { source.endsWith(it, !caseSensitive) }

  /**
   * 字符串是否以指定的任意一个子字符串结尾
   * ```
   * // return true
   * Strings.endsWith("123xyz456", "789", "456", "123");
   * // return false
   * Strings.endsWith("123xyz456", "abc", "xyz");
   * // return true
   * Strings.endsWith("abc123xyz", false, "123", "XYZ");
   * ```
   * @param source String 字符串
   * @param caseSensitive Boolean 区分大小写，默认true
   * @param postfixes Array<out String> 子字符串
   * @return Boolean 是否有一个子字符串是source的后缀
   */
  @JvmStatic
  @JvmOverloads
  fun endsWith(source: String, caseSensitive: Boolean = true, vararg postfixes: String) =
    endsWith(source, caseSensitive, postfixes.toList())

  /**
   * 确保字符串以给定的前缀开始
   * ```
   * // return "abcxyz"
   * Strings.insureStartsWith("xyz", "abc", true);
   * // return "abcABCxyz"
   * Strings.insureStartsWith("ABCxyz", "abc", true);
   * // return "ABCxyz"
   * Strings.insureStartsWith("ABCxyz", "abc", false);
   * ```
   * @param source String 字符串
   * @param prefix String 前缀
   * @param caseSensitive Boolean 区分大小写，默认true
   * @return String 带有指定前缀的字符串
   */
  @JvmStatic
  @JvmOverloads
  fun insureStartsWith(source: String, prefix: String, caseSensitive: Boolean = true): String =
    if (source.startsWith(prefix, !caseSensitive))
      source
    else prefix + source

  /**
   * 确保字符串以给定的后缀结束
   * ```
   * // return "abcxyz"
   * Strings.insureEndsWith("abc", "xyz", true);
   * // return "abcXYZxyz"
   * Strings.insureEndsWith("abcXYZ", "xyz", true);
   * // return "abcXYZ"
   * Strings.insureEndsWith("abcXYZ", "xyz", false);
   * ```
   * @param source String 字符串
   * @param suffix String 后缀
   * @param caseSensitive Boolean 区分大小写，默认true
   * @return String 带有指定后缀的字符串
   */
  @JvmStatic
  @JvmOverloads
  fun insureEndsWith(source: String, suffix: String, caseSensitive: Boolean = true): String =
    if (source.endsWith(suffix, !caseSensitive))
      source
    else
      source + suffix

  // ===================================================================================================================

  /**
   * 获取字符串指定长度的缩略，尾部跟填充物
   * ```
   * // return "Install the plugi...";
   * TextHelper.brief("Install the plugin; Restart Eclipse and go to Window", 20, "...");
   * // return "默认逻辑是当表单验证失败时,把按钮...";
   * TextHelper.brief("默认逻辑是当表单验证失败时,把按钮给变灰色", 20, "...");
   * ```
   * @param source String 字符串
   * @param length Int 处理后字符串长度
   * @param filler String 填充物
   */
  @JvmStatic
  fun brief(source: String, length: Int, filler: String): String =
    if (filler.isEmpty())
      source.substring(0, length)
    else
      source.substring(0, length - filler.length) + filler

  // ===================================================================================================================

  /**
   * 使用给定的前缀与后缀包裹指定的子字符串
   * ```
   * // return "abc[123]xyz[123]"
   * Strings.wrap("abc123xyz123", "123", "[", "]");
   * ```
   * @param source String 字符串
   * @param looking4 String 需要处理的子字符串
   * @param prefix String 前缀
   * @param suffix String 后缀
   * @return String 处理后的字符串
   */
  @JvmStatic
  fun wrap(source: String, looking4: String, prefix: String, suffix: String): String {
    if (source.isEmpty()) return ""

    return source.replace(looking4, concat(prefix, looking4, suffix))
  }

  // ===================================================================================================================

  /**
   * 将字符串反转
   * ```
   * // return "321"
   * Strings.reverse("123")
   * // return "zyx_cba"
   * Strings.reverse("abc_xyz")
   * ```
   * @param source String 字符串
   * @return String 反转后的字符串
   */
  @JvmStatic
  fun reverse(source: String) =
    source.toCharArray().let {
      it.reverse()
      String(it)
    }

  // ===================================================================================================================

  /**
   * 获取字符串最后的部分，直到出现参数[looking4]为止
   * ```
   * // return "core"
   * Strings.lastUntil("/root/dean/core", "/")
   * // return "/core"
   * Strings.lastUntil("/root/dean/core", "/", true)
   * // return "/root/dean/core"
   * Strings.lastUntil("/root/dean/core", "=")
   * ```
   * @param source String 字符串
   * @param looking4 String 截取尾部字符串的标识字符
   * @param extend Boolean 为true时，结果字符串开头带有[looking4]
   * @return String 尾部的子字符串，如没有出现[looking4]，返回原字符串
   */
  @JvmStatic
  @JvmOverloads
  fun lastUntil(source: String, looking4: String, extend: Boolean = false): String {
    val idx = source.lastIndexOf(looking4)
    return when {
      idx < 0 -> source
      extend -> source.substring(idx)
      else -> source.substring(idx + looking4.length)
    }
  }

  /**
   * 获取字符串最后的部分，直到出现参数[looking4]为止
   * ```
   * // return "C:"
   * Strings.frontUntil("C:/windows/system32", "/")
   * // return "C:/"
   * Strings.frontUntil("C:/windows/system32", "/", true)
   * // return "C:/windows/system32"
   * Strings.frontUntil("C:/windows/system32", "=")
   * ```
   * @param source String 字符串
   * @param looking4 String 截取开头字符串的标识字符
   * @param extend Boolean 为true时，结果字符串结尾带有[looking4]
   * @return String 开头的子字符串，如没有出现[looking4]，返回原字符串
   */
  @JvmStatic
  @JvmOverloads
  fun frontUntil(source: String, looking4: String, extend: Boolean = false): String {
    val idx = source.indexOf(looking4)

    return when {
      idx < 0 -> source
      idx == 0 && extend -> looking4
      idx == 0 -> ""
      extend -> source.substring(0, idx + looking4.length)
      else -> source.substring(0, idx)
    }
  }

  // ===================================================================================================================

  /**
   * 判断与任意一个字符串是否相等，区分大小写
   * ```
   * // return true
   * Strings.equals("1", "1", "2", "3")
   * // reutrn false
   * Strings.equals("1", "2", "3", "4")
   * ```
   * @param source String 字符串
   * @param targets String 判断相等的字符串目标数组
   * @return Boolean [targets]中有任一字符串与[source]相等，返回true
   */
  @JvmStatic
  fun equalsAny(source: String, vararg targets: String?): Boolean =
    targets.any { source == it }

  /**
   * 判断与任意一个字符串是否相等，不区分大小写
   * 参考[equals]
   */
  @JvmStatic
  fun equalsAnyIgnoreCase(source: String, vararg targets: String?): Boolean =
    targets.any { source.equals(it, true) }

}
