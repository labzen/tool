package cn.labzen.tool.feature.tries;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单代码块尝试器，无输出也无输入
 */
public class JustTrier {

  private final List<Runnable> attemptBlocs = new ArrayList<>();

  public JustTrier attempt(Runnable block) {
    attemptBlocs.add(block);
    return this;
  }

  public void execute() {
    for (Runnable block : attemptBlocs) {
      try {
        block.run();
        break;
      } catch (Exception e) {
        // do nothing
      }
    }
  }
}
