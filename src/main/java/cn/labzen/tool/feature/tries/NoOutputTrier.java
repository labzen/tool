package cn.labzen.tool.feature.tries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 无返回值尝试器，只接收一个输入值
 * <p>
 * 根据某一个值，顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，结束
 *
 * @param <I> 输入
 */
public class NoOutputTrier<I> {

  private final I input;
  private final List<Consumer<I>> attemptBlocs = new ArrayList<>();

  public NoOutputTrier(I input) {
    this.input = input;
  }

  public NoOutputTrier<I> attempt(Consumer<I> block) {
    attemptBlocs.add(block);
    return this;
  }

  public void execute() {
    for (Consumer<I> block : attemptBlocs) {
      try {
        block.accept(input);
        break;
      } catch (Exception e) {
        // do nothing
      }
    }
  }
}
