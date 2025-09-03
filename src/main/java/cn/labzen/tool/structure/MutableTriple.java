package cn.labzen.tool.structure;

/**
 * Triple三元组（值可变）
 */
public final class MutableTriple<F, S, T> {

  private F first;
  private S second;
  private T third;

  public MutableTriple(F first, S second, T third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public Triple<F, S, T> immutable() {
    return new Triple<>(first, second, third);
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

  public T third() {
    return third;
  }

  public void third(T third) {
    this.third = third;
  }
}
