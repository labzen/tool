package cn.labzen.tool.exception;

import cn.labzen.meta.exception.LabzenRuntimeException;

/**
 * 动态链接库加载异常
 */
public class LibraryLoadException extends LabzenRuntimeException {

  public LibraryLoadException(String message, Object... args) {
    super(message, args);
  }

  public LibraryLoadException(String message) {
    super(message);
  }

  public LibraryLoadException(Throwable cause) {
    super(cause);
  }

  public LibraryLoadException(Throwable cause, String message) {
    super(cause, message);
  }

  public LibraryLoadException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}
