@file:Suppress("unused")

package cn.labzen.tool.bean

import java.io.Serializable

/**
 * Triple三元组（值不可变）
 * @param F 第一个元素类型，可为空
 * @param S 第二个元素类型，可为空
 * @param T 第三个元素类型，可为空
 */
data class Triple<F, S, T> constructor(val first: F?, val second: S?, val third: T?) : Serializable {
  fun copy() = Triple(first, second, third)
  fun toMutableTriple() = MutableTriple(first, second, third)
}

/**
 * Triple三元组（值可变）
 * @param F 第一个元素类型，可为空
 * @param S 第二个元素类型，可为空
 * @param T 第三个元素类型，可为空
 */
data class MutableTriple<F, S, T> constructor(var first: F?, var second: S?, var third: T?) : Serializable {
  fun copy() = MutableTriple(first, second, third)
  fun toTriple() = Triple(first, second, third)
}

/**
 * Triple三元组（绝对不为空，值不可变）
 * @param F 第一个元素类型，不能为空
 * @param S 第二个元素类型，不能为空
 * @param T 第三个元素类型，不能为空
 */
data class StrictTriple<F, S, T> constructor(val first: F, val second: S, val third: T) : Serializable {

  init {
    if (first == null || second == null || third == null) {
      throw NullPointerException("StrictTriple constructor arguments can not be null value.")
    }
  }

  fun copy() = StrictTriple(first, second, third)
  fun toMutableTriple() = StrictMutableTriple(first, second, third)
}

/**
 * Triple三元组（绝对不为空，值可变）
 * @param F 第一个元素类型，不能为空
 * @param S 第二个元素类型，不能为空
 * @param T 第三个元素类型，不能为空
 */
data class StrictMutableTriple<F, S, T> constructor(var first: F, var second: S, var third: T) : Serializable {

  init {
    if (first == null || second == null || third == null) {
      throw NullPointerException("StrictMutableTriple constructor arguments can not be null value.")
    }
  }

  fun copy() = StrictMutableTriple(first, second, third)
  fun toTriple() = StrictTriple(first, second, third)
}
