package cn.labzen.tool.feature.tries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 普通尝试器，带有输入输出
 * <p>
 * 根据某一个值，顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，返回该代码块的结果
 *
 * @param <I> 输入
 * @param <O> 输出
 */
public class NormalTrier<I, O> {

  private final I input;
  private final List<Function<I, O>> attemptBlocs = new ArrayList<>();

  public NormalTrier(I input) {
    this.input = input;
  }

  public NormalTrier<I, O> attempt(Function<I, O> block) {
    attemptBlocs.add(block);
    return this;
  }

  /**
   * @return 如果全部尝试失败，返回null
   */
  public O execute() {
    for (Function<I, O> block : attemptBlocs) {
      try {
        return block.apply(input);
      } catch (Exception e) {
        // do nothing
      }
    }
    return null;
  }
}
