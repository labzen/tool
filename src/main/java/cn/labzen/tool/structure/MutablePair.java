package cn.labzen.tool.structure;

/**
 * Pair键值对（值可变）
 */
public final class MutablePair<F, S> {

  private F first;
  private S second;

  public MutablePair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public Pair<F, S> immutable() {
    return new Pair<>(first, second);
  }

  public F first() {
    return first;
  }

  public void first(F first) {
    this.first = first;
  }

  public S second() {
    return second;
  }

  public void second(S second) {
    this.second = second;
  }
}
