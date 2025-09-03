package cn.labzen.tool.exception;

import cn.labzen.meta.exception.LabzenRuntimeException;

/**
 * 文件处理异常
 */
public class FileException extends LabzenRuntimeException {

  public FileException(String message) {
    super(message);
  }

  public FileException(String message, Object... args) {
    super(message, args);
  }

  public FileException(Throwable cause) {
    super(cause);
  }

  public FileException(Throwable cause, String message) {
    super(cause, message);
  }

  public FileException(Throwable cause, String message, Object... args) {
    super(cause, message, args);
  }
}
