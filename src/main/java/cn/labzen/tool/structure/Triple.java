package cn.labzen.tool.structure;

/**
 * Triple三元组（值不可变）
 */
public record Triple<F, S, T>(F first, S second, T third) {

  public Triple<F, S, T> copy() {
    return new Triple<>(first, second, third);
  }

  public Triple<F, S, T> copyFirst(S second, T third) {
    return new Triple<>(first, second, third);
  }

  public Triple<F, S, T> copySecond(F first, T third) {
    return new Triple<>(first, second, third);
  }

  public Triple<F, S, T> copyThird(F first, S second) {
    return new Triple<>(first, second, third);
  }

  public MutableTriple<F, S, T> mutable() {
    return new MutableTriple<>(first, second, third);
  }
}
