package cn.labzen.tool.feature.tries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 无输入值尝试器，只定义返回值
 * <p>
 * 顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，返回该代码块的结果
 *
 * @param <O> 输入
 */
public class NoInputTrier<O> {

  private final List<Supplier<O>> attemptBlocs = new ArrayList<>();

  public NoInputTrier<O> attempt(Supplier<O> block) {
    attemptBlocs.add(block);
    return this;
  }

  /**
   * @return 如果全部尝试失败，返回null
   */
  public O execute() {
    for (Supplier<O> block : attemptBlocs) {
      try {
        return block.get();
      } catch (Exception e) {
        // do nothing
      }
    }
    return null;
  }
}
