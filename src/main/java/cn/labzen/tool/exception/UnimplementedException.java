package cn.labzen.tool.exception;

import cn.labzen.meta.exception.LabzenRuntimeException;

/**
 * 功能未实现异常
 */
public class UnimplementedException extends LabzenRuntimeException {

  public UnimplementedException(String message) {
    super(message);
  }

  public UnimplementedException(String message, Object... args) {
    super(message, args);
  }

  public UnimplementedException(Throwable cause) {
    super(cause);
  }

  public UnimplementedException(Throwable cause, String message) {
    super(cause, message);
  }

  public UnimplementedException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}
