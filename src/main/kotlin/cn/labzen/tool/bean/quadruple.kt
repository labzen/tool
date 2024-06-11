package cn.labzen.tool.bean

import java.io.Serializable


/**
 * Quadruple四元组（值不可变）
 * @param F 第一个元素类型，可为空
 * @param S 第二个元素类型，可为空
 * @param T 第三个元素类型，可为空
 * @param U 第四个元素类型，可为空
 */
data class Quadruple<F, S, T, U>
constructor(val first: F?, val second: S?, val third: T?, val fourth: U?) : Serializable {
  fun copy() = Quadruple(first, second, third, fourth)
  fun toMutableQuadruple() = MutableQuadruple(first, second, third, fourth)
}

/**
 * Quadruple四元组（值可变）
 * @param F 第一个元素类型，可为空
 * @param S 第二个元素类型，可为空
 * @param T 第三个元素类型，可为空
 * @param U 第四个元素类型，可为空
 */
data class MutableQuadruple<F, S, T, U>
constructor(var first: F?, var second: S?, var third: T?, var fourth: U?) : Serializable {
  fun copy() = MutableQuadruple(first, second, third, fourth)
  fun toQuadruple() = Quadruple(first, second, third, fourth)
}

/**
 * Quadruple四元组（绝对不为空，值不可变）
 * @param F 第一个元素类型，不能为空
 * @param S 第二个元素类型，不能为空
 * @param T 第三个元素类型，不能为空
 * @param U 第四个元素类型，不能为空
 */
data class StrictQuadruple<F, S, T, U>
constructor(val first: F, val second: S, val third: T, val fourth: U) : Serializable {

  init {
    if (first == null || second == null || third == null || fourth == null) {
      throw NullPointerException("StrictQuadruple constructor arguments can not be null value.")
    }
  }

  fun copy() = StrictQuadruple(first, second, third, fourth)
  fun toMutableQuadruple() = StrictMutableQuadruple(first, second, third, fourth)
}

/**
 * Quadruple四元组（绝对不为空，值可变）
 * @param F 第一个元素类型，不能为空
 * @param S 第二个元素类型，不能为空
 * @param T 第三个元素类型，不能为空
 * @param U 第四个元素类型，不能为空
 */
data class StrictMutableQuadruple<F, S, T, U>
constructor(var first: F, var second: S, var third: T, var fourth: U) : Serializable {

  init {
    if (first == null || second == null || third == null || fourth == null) {
      throw NullPointerException("StrictMutableQuadruple constructor arguments can not be null value.")
    }
  }

  fun copy() = StrictMutableQuadruple(first, second, third, fourth)
  fun toQuadruple() = StrictQuadruple(first, second, third, fourth)
}
