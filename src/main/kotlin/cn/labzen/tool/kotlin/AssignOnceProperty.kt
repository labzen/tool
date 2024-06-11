package cn.labzen.tool.kotlin

import java.util.function.Supplier

/**
 * 保证只能被赋值一次的属性
 */
class AssignOnceProperty<T> {

  private var value: T? = null

  fun get(): T =
    value ?: throw IllegalStateException("属性未被赋值")

  fun getOrNull() =
    value

  fun getOrElse(other: T): T =
    value ?: other

  fun getOrElse(supplier: Supplier<T>): T =
    value ?: supplier.get()

  fun set(value: T) {
    this.value?.apply { throw IllegalStateException("属性已赋值过一次，无法重复赋值") }
    this.value = value
  }

  fun setIfAbsent(value: T): T =
    this.value ?: value.also { this.value = it }

  fun assigned(): Boolean =
    this.value != null
}
