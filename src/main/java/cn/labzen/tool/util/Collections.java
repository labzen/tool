package cn.labzen.tool.util;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Collections {

  private Collections() {
  }

  /**
   * 集合是 NULL 或 0 元素
   */
  public static boolean isNullOrEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  /**
   * 剔除集合中的 null 元素
   */
  public static <E> List<E> removeNullElement(List<E> list) {
    if (list == null) {
      return java.util.Collections.emptyList();
    }
    return list.stream().filter(java.util.Objects::nonNull).toList();
  }

  /**
   * 剔除集合中的 null 值与空内容字符串
   */
  public static List<String> removeBlankElement(List<String> list) {
    if (list == null) {
      return java.util.Collections.emptyList();
    }
    return list.stream().filter(java.util.Objects::nonNull).filter(s -> !s.isBlank()).toList();
  }

  /**
   * 如果集合中所有元素相同，返回 true
   */
  public static <E, K> boolean allSame(Collection<E> collection, Function<E, K> selector) {
    if (collection == null || collection.isEmpty()) {
      return false;
    }
    Set<Object> set = collection.stream()
                                .filter(java.util.Objects::nonNull)
                                .map(e -> selector == null ? e : selector.apply(e))
                                .collect(Collectors.toSet());
    return set.size() <= 1;
  }

  /**
   * 保证List集合中包含指定元素
   */
  public static <E> void insureListContains(List<E> collection, E element) {
    if (collection == null || collection.contains(element)) {
      return;
    }

    collection.add(element);
  }

  /**
   * 保证Set集合中包含指定元素
   */
  public static <E> void insureSetContains(Set<E> collection, E element) {
    if (collection == null || collection.contains(element)) {
      return;
    }

    collection.add(element);
  }

  /**
   * 如果List集合为null或空，返回包含指定元素的集合，否则原样返回
   */
  public static <E> List<E> oneElementListWhenEmpty(List<E> collection, E element) {
    if (collection != null && !collection.isEmpty()) {
      return collection;
    }

    return Lists.newArrayList(element);
  }

  /**
   * 如果Set集合为null或空，返回包含指定元素的集合，否则原样返回
   */
  public static <E> Set<E> oneElementSetWhenEmpty(Set<E> collection, E element) {
    if (collection != null && !collection.isEmpty()) {
      return collection;
    }

    HashSet<E> result = new HashSet<>();
    result.add(element);
    return result;
  }

}
