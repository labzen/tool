package cn.labzen.tool.structure;

/**
 * Pair键值对（值不可变）
 */
public record Pair<F, S>(F first, S second) {

  public Pair<F, S> copy() {
    return new Pair<>(first, second);
  }

  public Pair<F, S> copyFirst(S second) {
    return new Pair<>(first, second);
  }

  public Pair<F, S> copySecond(F first) {
    return new Pair<>(first, second);
  }

  public MutablePair<F, S> mutable() {
    return new MutablePair<>(first, second);
  }
}
