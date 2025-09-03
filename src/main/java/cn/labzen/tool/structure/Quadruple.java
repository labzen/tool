package cn.labzen.tool.structure;

/**
 * Quadruple四元组（值不可变）
 */
public record Quadruple<F, S, T, R>(F first, S second, T third, R fourth) {

  public Quadruple<F, S, T, R> copy() {
    return new Quadruple<>(first, second, third, fourth);
  }

  public Quadruple<F, S, T, R> copyFirst(S second, T third, R fourth) {
    return new Quadruple<>(first, second, third, fourth);
  }

  public Quadruple<F, S, T, R> copySecond(F first, T third, R fourth) {
    return new Quadruple<>(first, second, third, fourth);
  }

  public Quadruple<F, S, T, R> copyThird(F first, S second, R fourth) {
    return new Quadruple<>(first, second, third, fourth);
  }

  public Quadruple<F, S, T, R> copyFourth(F first, S second, T third) {
    return new Quadruple<>(first, second, third, fourth);
  }

  public MutableQuadruple<F, S, T, R> mutable() {
    return new MutableQuadruple<>(first, second, third, fourth);
  }
}
