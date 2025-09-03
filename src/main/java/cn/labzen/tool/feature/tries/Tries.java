package cn.labzen.tool.feature.tries;

/**
 * 多个try代码块的顺序尝试，直至成功
 * <p>
 * 适用于当有相近的多个功能代码块需要执行，但代码块会抛出异常；需求上几个代码块之间属于互斥，即第一个代码块执行成功，后续的代码块不需要再执行
 * <p>
 * 例如，当需要对一个日期字符串进行解析时，因不知道字符串的日期格式，又想兼容多个格式，则需要进行很多已知格式的逐个尝试。但日期格式的解析会抛出异常，
 * 如果用常规的代码结构编写，则会出现很多的try-catch块，而且需要记录每次的执行结果，进行判断。使用本工具类，会简化代码结构
 * <hr/>
 * s
 */
public final class Tries {

  /**
   * 根据某一个值，顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，返回该代码块的结果
   */
  public static <I, O> NormalTrier<I, O> with(I input) {
    return new NormalTrier<>(input);
  }

  /**
   * 根据某一个值，顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，结束
   */
  public static <I> NoOutputTrier<I> withNoReturn(I input) {
    return new NoOutputTrier<>(input);
  }

  /**
   * 顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，返回该代码块的结果
   *
   * @return 如果全部尝试失败，返回null
   */
  public static <O> NoInputTrier<O> withNoValue() {
    return new NoInputTrier<>();
  }

  /**
   * 顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，结束
   */
  public static JustTrier justTry() {
    return new JustTrier();
  }
}
