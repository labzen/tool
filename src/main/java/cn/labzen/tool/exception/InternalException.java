package cn.labzen.tool.exception;

import cn.labzen.meta.exception.LabzenException;

/**
 * 内部错误
 */
public class InternalException extends LabzenException {

  public InternalException(String message) {
    super(message);
  }

  public InternalException(String message, Object... args) {
    super(message, args);
  }

  public InternalException(Throwable cause) {
    super(cause);
  }

  public InternalException(Throwable cause, String message) {
    super(cause, message);
  }

  public InternalException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}
