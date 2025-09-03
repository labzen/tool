package cn.labzen.tool.exception;

import cn.labzen.meta.exception.LabzenRuntimeException;

/**
 * 字符串处理异常
 */
public class StringException extends LabzenRuntimeException {

  public StringException(String message) {
    super(message);
  }

  public StringException(String message, Object... args) {
    super(message, args);
  }

  public StringException(Throwable cause) {
    super(cause);
  }

  public StringException(Throwable cause, String message) {
    super(cause, message);
  }

  public StringException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}
