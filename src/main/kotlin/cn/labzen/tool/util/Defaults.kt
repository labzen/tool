package cn.labzen.tool.util

object Defaults {

  @JvmStatic
  fun stringValue(value: String?, default: String): String =
    value ?: default

  @JvmStatic
  fun intValue(value: Int?, default: Int): Int =
    value ?: default

  @JvmStatic
  fun longValue(value: Long?, default: Long): Long =
    value ?: default

  @JvmStatic
  fun shortValue(value: Short?, default: Short): Short =
    value ?: default

  @JvmStatic
  fun doubleValue(value: Double?, default: Double): Double =
    value ?: default

  @JvmStatic
  fun floatValue(value: Float?, default: Float): Float =
    value ?: default

  @JvmStatic
  fun booleanValue(value: Boolean?, default: Boolean): Boolean =
    value ?: default

  @JvmStatic
  fun <T> objectValue(value: T?, default: T): T =
    value ?: default
}
