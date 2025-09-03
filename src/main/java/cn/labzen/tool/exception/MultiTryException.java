package cn.labzen.tool.exception;

import cn.labzen.meta.exception.LabzenRuntimeException;

/**
 * Try执行异常
 */
public class MultiTryException extends LabzenRuntimeException {

  public MultiTryException(String message) {
    super(message);
  }

  public MultiTryException(String message, Object... args) {
    super(message, args);
  }

  public MultiTryException(Throwable cause) {
    super(cause);
  }

  public MultiTryException(Throwable cause, String message) {
    super(cause, message);
  }

  public MultiTryException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}
