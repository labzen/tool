package cn.labzen.tool.util

import java.util.Collections

object Collections {

  /**
   * 集合是NULL或0元素
   */
  @JvmStatic
  fun isNullOrEmpty(collection: Collection<*>?) =
    collection?.toList()?.isEmpty() ?: true

  /**
   * 剔除集合中的null元素，如果参数为空，返回空集合
   * @param collection Iterable<E>? 集合
   * @return List<E> 剔除null后的集合，可能是空集合，但不会为null
   */
  @JvmStatic
  @Deprecated("定位不是很准确", ReplaceWith("this.removeNullElement(List)"))
  fun <E : Any> removeNull(collection: Iterable<E?>?): List<E> =
    collection?.filterNotNull() ?: Collections.emptyList()

  /**
   * 剔除集合中的null元素
   * @param list List<E> 集合
   * @return List<E> 剔除null后的集合，可能是空集合，但不会为null
   */
  @JvmStatic
  fun <E : Any> removeNullElement(list: List<E?>): List<E> =
    list.filterNotNull()

  /**
   * 剔除集合中的null值与空内容字符串，如果参数为空，返回空集合
   * @param collection Iterable<String>? 集合
   * @return List<E> 剔除后的集合，可能是空集合，但不会为null
   */
  @JvmStatic
  @Deprecated("定位不是很准确", ReplaceWith("this.removeBlankElement(List)"))
  fun removeBlank(collection: Iterable<String?>?): List<String> =
    collection?.filterNotNull()?.filterNot { it.isBlank() } ?: Collections.emptyList()

  /**
   * 剔除集合中的null值与空内容字符串
   * @param list List<String> 集合
   * @return List<String> 剔除后的集合，可能是空集合，但不会为null
   */
  @JvmStatic
  fun removeBlankElement(list: List<String?>): List<String> =
    list.filterNotNull().filterNot { it.isBlank() }

  @JvmStatic
  @Deprecated("定位不是很准确", ReplaceWith("this.allSame(Iterable, block)"))
  fun <E : Any, K> allEquals(collection: Iterable<E?>?, selector: (E) -> K): Boolean {
    collection ?: return false

    return collection.map {
      if (it == null) null else selector(it)
    }.toSet().size == 1
  }

  /**
   * 如果集合中所有的元素相同，返回true
   * @param collection Iterable<E> 集合
   * @param selector 选择器，挑选判断的目标
   * @return Boolean 如果所有元素全部相同
   */
  @JvmStatic
  fun <E : Any, K> allSame(collection: Collection<E?>, selector: ((E) -> K)? = null): Boolean =
    collection.filterNotNull().map {
      if (selector == null) {
        it
      } else {
        selector(it)
      }
    }.toSet().size <= 1

  @JvmStatic
  fun <C : Collection<E?>, E : Any> insureContains(collection: C, element: E): C =
    @Suppress("UNCHECKED_CAST")
    if (collection.contains(element)) {
      collection
    } else if (collection is List<*>) {
      collection.toMutableList().also {
        it.add(element)
      } as C
    } else {
      collection.toMutableSet().also {
        it.add(element)
      } as C
    }

  @JvmStatic
  fun <C : Collection<E?>, E : Any> onValueAtLeast(collection: C, element: E): C =
    collection.ifEmpty {
      @Suppress("UNCHECKED_CAST")
      if (collection is List<*>) {
        collection.toMutableList().also { it.add(element) } as C
      } else {
        collection.toMutableSet().also { it.add(element) } as C
      }
    }

}
