@file:Suppress("unused")

package cn.labzen.tool.bean

import java.io.Serializable

/**
 * Pair键值对（值不可变）
 * @param F 第一个元素类型，可为空
 * @param S 第二个元素类型，可为空
 */
data class Pair<F, S>(val first: F?, val second: S?) : Serializable {
  fun copy() = Pair(first, second)
  fun toMutablePair() = MutablePair(first, second)
}

/**
 * Pair键值对（值可变）
 * @param F 第一个元素类型，可为空
 * @param S 第二个元素类型，可为空
 */
data class MutablePair<F, S>(var first: F?, var second: S?) : Serializable {
  fun copy() = MutablePair(first, second)
  fun toPair() = Pair(first, second)
}

/**
 * Pair键值对（绝对不为空，值不可变）
 * @param F 第一个元素类型，不能为空
 * @param S 第二个元素类型，不能为空
 */
data class StrictPair<F, S>(val first: F, val second: S) : Serializable {

  init {
    if (first == null || second == null) {
      throw NullPointerException("StrictPair constructor arguments can not be null value.")
    }
  }

  fun copy() = StrictPair(first, second)
  fun toMutablePair() = StrictMutablePair(first, second)
}

/**
 * Pair键值对（绝对不为空，值可变）
 * @param F 第一个元素类型，不能为空
 * @param S 第二个元素类型，不能为空
 */
data class StrictMutablePair<F, S>(var first: F, var second: S) : Serializable {

  init {
    if (first == null || second == null) {
      throw NullPointerException("StrictMutablePair constructor arguments can not be null value.")
    }
  }

  fun copy() = StrictMutablePair(first, second)
  fun toPair() = StrictPair(first, second)
}
