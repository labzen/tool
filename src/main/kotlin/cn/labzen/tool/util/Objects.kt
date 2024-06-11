package cn.labzen.tool.util

import java.util.*
import java.util.function.Supplier

object Objects {

  private const val DEFAULT_RADIX = 10

  /**
   * 参数全部为空
   */
  @JvmStatic
  fun isAllNull(vararg objs: Any?) = objs.all { it == null }

  /**
   * 参数全部不为空
   */
  @JvmStatic
  fun isAllNotNull(vararg objs: Any?) = objs.all { it != null }

  /**
   * 参数要么全为空，要么全不为空
   */
  @JvmStatic
  fun isAllNullOrNot(vararg objs: Any?) = objs.all { it != null } || objs.all { it == null }

  /**
   * 参数中有空值
   */
  @JvmStatic
  fun isAnyNull(vararg objs: Any?) = objs.any { it == null }

  /**
   * 参数中有非空值
   */
  @JvmStatic
  fun isAnyNotNull(vararg objs: Any?) = objs.any { it != null }

  /**
   * 参数中的第几项为空
   */
  @JvmStatic
  fun isNullAt(index: Int, vararg objs: Any?) = objs.lastIndex >= index && objs[index] == null

  /**
   * 第一个参数为空，第二个参数不为空
   */
  @JvmStatic
  fun isLeftNull(left: Any?, right: Any?) = left == null && right != null

  /**
   * 第一个参数不为空，第二个参数为空
   */
  @JvmStatic
  fun isRightNull(left: Any?, right: Any?) = left != null && right == null

  /**
   * 返回第一个不为空的对象，如果全部为空，返回null
   */
  @JvmStatic
  fun firstNonNull(vararg os: Any?) =
    os.first { it != null }

  /**
   * 判断相等，否则抛异常
   */
  @JvmStatic
  fun equalsOrThrow(first: Any?, second: Any?, supplier: Supplier<Exception>) {
    if (first != second && first?.equals(second) != true) {
      throw supplier.get()
    }
  }

  /**
   * 判断相等，否则执行某段代码
   */
  @JvmStatic
  fun equalsOrElse(first: Any?, second: Any?, function: () -> Any?): Any? =
    if (first != second && first?.equals(second) != true) {
      function()
    } else null

  /**
   * 不为空，则抛异常
   */
  @JvmStatic
  fun notNullOrThrow(o: Any?, supplier: Supplier<Exception>) {
    o ?: throw supplier.get()
  }

  @JvmStatic
  @JvmOverloads
  fun canBeInt(s: String?, def: Int? = null) =
    s?.let {
      val result = canBeLong(it, def?.toLong())
      if (result != null && result == result.toInt().toLong()) {
        result.toInt()
      } else {
        null
      }
    } ?: def

  @JvmStatic
  @JvmOverloads
  fun canBeLong(s: String?, def: Long? = null) =
    s?.let {
      if (s.isBlank()) {
        return def
      }

      // 判断负数
      val negative = s[0] == '-'
      var index = if (negative) 1 else 0
      if (index == s.length) {
        return def
      }

      var digit = AsciiDigits.digit(s[index++])
      if (digit < 0 || digit >= DEFAULT_RADIX) {
        return def
      }
      var accum = (-digit).toLong()

      val cap = java.lang.Long.MIN_VALUE / DEFAULT_RADIX

      while (index < s.length) {
        digit = AsciiDigits.digit(s[index++])
        if (digit < 0 || digit >= DEFAULT_RADIX || accum < cap) {
          return def
        }
        accum *= DEFAULT_RADIX.toLong()
        if (accum < java.lang.Long.MIN_VALUE + digit) {
          return def
        }
        accum -= digit.toLong()
      }

      return when {
        negative -> accum
        accum == Long.MIN_VALUE -> def
        else -> -accum
      }
    } ?: def

  internal object AsciiDigits {

    private val asciiDigits: ByteArray = ByteArray(128)

    init {
      Arrays.fill(asciiDigits, (-1).toByte())
      for (i in 0..9) {
        asciiDigits['0'.code + i] = i.toByte()
      }
      for (i in 0..26) {
        asciiDigits['A'.code + i] = (10 + i).toByte()
        asciiDigits['a'.code + i] = (10 + i).toByte()
      }
    }

    fun digit(c: Char): Int =
      (if (c.code < 128) asciiDigits[c.code] else -1).toInt()
  }
}
