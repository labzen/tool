package cn.labzen.tool.exception;

import cn.labzen.meta.exception.LabzenRuntimeException;

/**
 * 随机处理异常
 */
public class RandomException extends LabzenRuntimeException {

  public RandomException(String message) {
    super(message);
  }

  public RandomException(String message, Object... args) {
    super(message, args);
  }

  public RandomException(Throwable cause) {
    super(cause);
  }

  public RandomException(Throwable cause, String message) {
    super(cause, message);
  }

  public RandomException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}
