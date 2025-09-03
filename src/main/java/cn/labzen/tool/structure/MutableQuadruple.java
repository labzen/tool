package cn.labzen.tool.structure;

/**
 * Triple三元组（值可变）
 */
public final class MutableQuadruple<F, S, T, R> {

  private F first;
  private S second;
  private T third;
  private R fourth;

  public MutableQuadruple(F first, S second, T third, R fourth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
  }

  public Quadruple<F, S, T, R> immutable() {
    return new Quadruple<>(first, second, third, fourth);
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

  public R fourth() {
    return fourth;
  }

  public void fourth(R fourth) {
    this.fourth = fourth;
  }
}
