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
  public static <E> void insureContains(List<E> collection, E element) {
    if (collection == null || collection.contains(element)) {
      return;
    }

    collection.add(element);
  }

  /**
   * 保证Set集合中包含指定元素
   */
  public static <E> void insureContains(Set<E> collection, E element) {
    if (collection == null || collection.contains(element)) {
      return;
    }

    collection.add(element);
  }

  /**
   * 如果List集合为null或空，返回包含指定元素的集合，否则原样返回
   */
  public static <E> List<E> addIfEmpty(List<E> collection, E element) {
    if (collection != null && !collection.isEmpty()) {
      return collection;
    }

    return Lists.newArrayList(element);
  }

  /**
   * 如果Set集合为null或空，返回包含指定元素的集合，否则原样返回
   */
  public static <E> Set<E> addIfEmpty(Set<E> collection, E element) {
    if (collection != null && !collection.isEmpty()) {
      return collection;
    }

    HashSet<E> result = new HashSet<>();
    result.add(element);
    return result;
  }

  /**
   * 将 {@link List} 转换为有序 {@link LinkedHashMap}（保持插入顺序），key 由指定函数生成，value 为元素本身。
   * 遇到重复 key 时保留后出现的值。
   *
   * <pre>{@code
   * List<User> users = List.of(u1, u2, u3);
   * Map<Long, User> map = Collections.toMap(users, User::getId);
   * }</pre>
   *
   * @param collection 源集合，为 null 或空时返回空 Map
   * @param keySelector key 生成函数
   * @param <E> 集合元素类型
   * @param <K> Map 的 key 类型
   * @return 有序 LinkedHashMap
   */
  public static <E, K> Map<K, E> toMap(List<E> collection, Function<E, K> keySelector) {
    if (isNullOrEmpty(collection) || keySelector == null) {
      return java.util.Collections.emptyMap();
    }
    return collection.stream().collect(Collectors.toMap(keySelector, e -> e, (a, b) -> b, LinkedHashMap::new));
  }

  /**
   * 将 {@link List} 转换为有序 {@link LinkedHashMap}（保持插入顺序），key 和 value 均由指定函数生成。
   * 遇到重复 key 时保留后出现的值。
   *
   * <pre>{@code
   * List<Order> orders = List.of(o1, o2);
   * Map<String, BigDecimal> amountMap = Collections.toMap(orders, Order::getOrderNo, Order::getAmount);
   * }</pre>
   *
   * @param collection   源集合，为 null 或空时返回空 Map
   * @param keySelector   key 生成函数
   * @param valueSelector value 生成函数
   * @param <E> 集合元素类型
   * @param <K> Map 的 key 类型
   * @param <V> Map 的 value 类型
   * @return 有序 LinkedHashMap
   */
  public static <E, K, V> Map<K, V> toMap(List<E> collection,
                                          Function<E, K> keySelector,
                                          Function<E, V> valueSelector) {
    if (isNullOrEmpty(collection) || keySelector == null || valueSelector == null) {
      return java.util.Collections.emptyMap();
    }
    return collection.stream()
                     .collect(Collectors.toMap(keySelector, valueSelector, (a, b) -> b, LinkedHashMap::new));
  }

  /**
   * 将 {@link Set} 转换为 {@link HashMap}，key 由指定函数生成，value 为元素本身。
   * 遇到重复 key 时保留后出现的值。
   *
   * <pre>{@code
   * Set<Product> products = Set.of(p1, p2);
   * Map<String, Product> map = Collections.toMap(products, Product::getSku);
   * }</pre>
   *
   * @param collection  源集合，为 null 或空时返回空 Map
   * @param keySelector key 生成函数
   * @param <E> 集合元素类型
   * @param <K> Map 的 key 类型
   * @return HashMap
   */
  public static <E, K> Map<K, E> toMap(Set<E> collection, Function<E, K> keySelector) {
    if (isNullOrEmpty(collection) || keySelector == null) {
      return java.util.Collections.emptyMap();
    }
    return collection.stream().collect(Collectors.toMap(keySelector, e -> e, (a, b) -> b, HashMap::new));
  }

  /**
   * 将 {@link Set} 转换为 {@link HashMap}，key 和 value 均由指定函数生成。
   * 遇到重复 key 时保留后出现的值。
   *
   * @param collection   源集合，为 null 或空时返回空 Map
   * @param keySelector   key 生成函数
   * @param valueSelector value 生成函数
   * @param <E> 集合元素类型
   * @param <K> Map 的 key 类型
   * @param <V> Map 的 value 类型
   * @return HashMap
   */
  public static <E, K, V> Map<K, V> toMap(Set<E> collection, Function<E, K> keySelector, Function<E, V> valueSelector) {
    if (isNullOrEmpty(collection) || keySelector == null || valueSelector == null) {
      return java.util.Collections.emptyMap();
    }
    return collection.stream()
                     .collect(Collectors.toMap(keySelector, valueSelector, (a, b) -> b, HashMap::new));
  }

  /**
   * 将 {@link List} 按指定 key 分组，返回有序 {@link LinkedHashMap}（保持插入顺序）。
   *
   * <pre>{@code
   * List<Order> orders = List.of(o1, o2, o3);
   * Map<Long, List<Order>> grouped = Collections.groupBy(orders, Order::getUserId);
   * }</pre>
   *
   * @param collection  源集合，为 null 或空时返回空 Map
   * @param keySelector key 生成函数
   * @param <E> 集合元素类型
   * @param <K> 分组 key 类型
   * @return 按 key 分组的 LinkedHashMap
   */
  public static <E, K> Map<K, List<E>> groupBy(List<E> collection, Function<E, K> keySelector) {
    if (isNullOrEmpty(collection) || keySelector == null) {
      return java.util.Collections.emptyMap();
    }
    return collection.stream().collect(Collectors.groupingBy(keySelector, LinkedHashMap::new, Collectors.toList()));
  }

  /**
   * 将 {@link List} 按指定 key 分组，并对每组元素进行 value 映射，返回有序 {@link LinkedHashMap}。
   *
   * <pre>{@code
   * List<Order> orders = List.of(o1, o2, o3);
   * Map<Long, List<String>> grouped = Collections.groupBy(orders, Order::getUserId, Order::getOrderNo);
   * }</pre>
   *
   * @param collection    源集合，为 null 或空时返回空 Map
   * @param keySelector   key 生成函数
   * @param valueSelector value 映射函数
   * @param <E> 集合元素类型
   * @param <K> 分组 key 类型
   * @param <V> 分组后 value 类型
   * @return 按 key 分组的 LinkedHashMap
   */
  public static <E, K, V> Map<K, List<V>> groupBy(List<E> collection,
                                                  Function<E, K> keySelector,
                                                  Function<E, V> valueSelector) {
    if (isNullOrEmpty(collection) || keySelector == null || valueSelector == null) {
      return java.util.Collections.emptyMap();
    }
    return collection.stream()
                     .collect(Collectors.groupingBy(keySelector,
                         LinkedHashMap::new,
                         Collectors.mapping(valueSelector, Collectors.toList())));
  }

  /**
   * 安全获取 {@link List} 中指定下标的元素，下标越界或集合为 null 时返回默认值。
   *
   * <pre>{@code
   * List<String> list = List.of("a", "b");
   * Collections.safeGet(list, 5, "default")   // "default"
   * Collections.safeGet(list, 0, "default")   // "a"
   * }</pre>
   *
   * @param collection  源集合
   * @param index       下标
   * @param defaultValue 下标越界或集合为 null 时的默认值
   * @param <E> 集合元素类型
   * @return 元素本身或默认值
   */
  public static <E> E safeGet(List<E> collection, int index, E defaultValue) {
    if (collection == null || index < 0 || index >= collection.size()) {
      return defaultValue;
    }
    return collection.get(index);
  }

  /**
   * 安全获取 {@link List} 的第一个元素，集合为 null 或空时返回默认值。
   *
   * <pre>{@code
   * Collections.safeFirst(List.of("a", "b"), "default")   // "a"
   * Collections.safeFirst(null, "default")                 // "default"
   * }</pre>
   *
   * @param collection  源集合
   * @param defaultValue 集合为空时的默认值
   * @param <E> 集合元素类型
   * @return 第一个元素或默认值
   */
  public static <E> E safeFirst(List<E> collection, E defaultValue) {
    if (isNullOrEmpty(collection)) {
      return defaultValue;
    }
    return collection.get(0);
  }

  /**
   * 安全获取 {@link List} 的最后一个元素，集合为 null 或空时返回默认值。
   *
   * <pre>{@code
   * Collections.safeLast(List.of("a", "b"), "default")   // "b"
   * Collections.safeLast(null, "default")                 // "default"
   * }</pre>
   *
   * @param collection  源集合
   * @param defaultValue 集合为空时的默认值
   * @param <E> 集合元素类型
   * @return 最后一个元素或默认值
   */
  public static <E> E safeLast(List<E> collection, E defaultValue) {
    if (isNullOrEmpty(collection)) {
      return defaultValue;
    }
    return collection.get(collection.size() - 1);
  }

  /**
   * 将 {@link List} 按指定大小拆分为多个子 List（视图，基于原始列表的 subList）。
   *
   * <pre>{@code
   * List<Integer> list = List.of(1, 2, 3, 4, 5);
   * List<List<Integer>> page = Collections.partition(list, 2);  // [[1,2], [3,4], [5]]
   * }</pre>
   *
   * @param collection 源集合，为 null 或空时返回空 List
   * @param size       每批次的元素数量，必须大于 0
   * @param <E>        集合元素类型
   * @return 分区后的列表
   */
  public static <E> List<List<E>> partition(List<E> collection, int size) {
    if (isNullOrEmpty(collection) || size <= 0) {
      return java.util.Collections.emptyList();
    }
    int totalSize = collection.size();
    int partitionCount = (totalSize + size - 1) / size;
    List<List<E>> result = new ArrayList<>(partitionCount);
    for (int i = 0; i < totalSize; i += size) {
      result.add(collection.subList(i, Math.min(i + size, totalSize)));
    }
    return result;
  }

  /**
   * 将 {@link Set} 按指定大小拆分为多个子 {@link Set}（深拷贝，每个子 Set 为独立的新 {@link HashSet}）。
   *
   * <pre>{@code
   * Set<Integer> set = Set.of(1, 2, 3, 4, 5);
   * List<Set<Integer>> batches = Collections.partition(set, 2);  // [[1,2], [3,4], [5]]
   * }</pre>
   *
   * @param collection 源集合，为 null 或空时返回空 List
   * @param size       每批次的元素数量，必须大于 0
   * @param <E>        集合元素类型
   * @return 分区后的集合列表
   */
  public static <E> List<Set<E>> partition(Set<E> collection, int size) {
    if (isNullOrEmpty(collection) || size <= 0) {
      return java.util.Collections.emptyList();
    }
    List<E> list = new ArrayList<>(collection);
    int totalSize = list.size();
    int partitionCount = (totalSize + size - 1) / size;
    List<Set<E>> result = new ArrayList<>(partitionCount);
    for (int i = 0; i < totalSize; i += size) {
      result.add(new HashSet<>(list.subList(i, Math.min(i + size, totalSize))));
    }
    return result;
  }
}
