package cn.labzen.tool.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * IO 操作工具类，封装常用的文件读写、流操作与资源安全关闭等高频操作。
 */
public final class IOs {

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  private IOs() {
  }

  /**
   * 安全关闭一个或多个 {@link AutoCloseable} 资源，忽略关闭过程中的异常。
   *
   * <pre>{@code
   * InputStream in = null;
   * OutputStream out = null;
   * try {
   *   in = new FileInputStream("test.txt");
   *   out = new FileOutputStream("copy.txt");
   *   // ... do work
   * } finally {
   *   IOs.closeQuietly(in, out);
   * }
   * }</pre>
   *
   * @param closeable 需要关闭的资源，{@code null} 值会被跳过
   */
  public static void closeQuietly(AutoCloseable... closeable) {
    if (closeable == null) {
      return;
    }
    for (AutoCloseable c : closeable) {
      if (c != null) {
        try {
          c.close();
        } catch (Exception ignored) {
          // intentionally suppressed
        }
      }
    }
  }

  // ===================================================================================================================
  // 字节流操作
  // ===================================================================================================================

  /**
   * 将 {@link InputStream} 的所有字节读取到字节数组中，完成后自动关闭流。
   *
   * <pre>{@code
   * byte[] data = IOs.readAllBytes(new FileInputStream("data.bin"));
   * }</pre>
   *
   * @param input 输入流，不可为 {@code null}
   * @return 包含流中所有内容的字节数组
   * @throws IOException 读取过程中发生 IO 错误时抛出
   */
  public static byte[] readAllBytes(InputStream input) throws IOException {
    if (input == null) {
      return new byte[0];
    }
    try (input) {
      return input.readAllBytes();
    }
  }

  /**
   * 将文件的所有字节读取到字节数组中。
   *
   * @param path 文件路径
   * @return 包含文件所有内容的字节数组
   * @throws IOException 读取过程中发生 IO 错误时抛出
   */
  public static byte[] readAllBytes(Path path) throws IOException {
    if (path == null) {
      return new byte[0];
    }
    return Files.readAllBytes(path);
  }

  /**
   * 将字节数组写入文件。
   *
   * @param path  文件路径
   * @param bytes 要写入的字节数组
   * @throws IOException 写入过程中发生 IO 错误时抛出
   */
  public static void writeBytes(Path path, byte[] bytes) throws IOException {
    writeBytes(path, bytes, false);
  }

  /**
   * 将字节数组写入文件，可选择追加或覆写。
   *
   * @param path   文件路径
   * @param bytes  要写入的字节数组
   * @param append {@code true} 为追加模式，{@code false} 为覆写模式
   * @throws IOException 写入过程中发生 IO 错误时抛出
   */
  public static void writeBytes(Path path, byte[] bytes, boolean append) throws IOException {
    if (path == null || bytes == null) {
      return;
    }
    OpenOption[] options = append
        ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
        : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
    Files.write(path, bytes, options);
  }

  /**
   * 将 {@link InputStream} 的内容复制到 {@link OutputStream}，使用默认缓冲区大小。
   * 复制完成后流保持打开状态，调用方需要自行关闭。
   *
   * <pre>{@code
   * try (InputStream in = new FileInputStream("src");
   *      OutputStream out = new FileOutputStream("dst")) {
   *   IOs.copy(in, out);
   * }
   * }</pre>
   *
   * @param input  输入流
   * @param output 输出流
   * @return 复制的总字节数
   * @throws IOException 复制过程中发生 IO 错误时抛出
   */
  public static long copy(InputStream input, OutputStream output) throws IOException {
    return copy(input, output, DEFAULT_BUFFER_SIZE);
  }

  /**
   * 将 {@link InputStream} 的内容复制到 {@link OutputStream}，使用指定的缓冲区大小。
   *
   * @param input      输入流
   * @param output     输出流
   * @param bufferSize 缓冲区大小（字节）
   * @return 复制的总字节数
   * @throws IOException 复制过程中发生 IO 错误时抛出
   */
  public static long copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
    if (input == null || output == null) {
      return 0;
    }
    byte[] buffer = new byte[Math.max(bufferSize, 1)];
    long total = 0;
    int n;
    while ((n = input.read(buffer)) != -1) {
      output.write(buffer, 0, n);
      total += n;
    }
    output.flush();
    return total;
  }

  // ===================================================================================================================
  // 文本操作
  // ===================================================================================================================

  /**
   * 读取文件全部行到 {@link List} 中，使用 UTF-8 编码。
   *
   * <pre>{@code
   * List<String> lines = IOs.readAllLines(Path.of("config.txt"));
   * }</pre>
   *
   * @param path 文件路径
   * @return 文件中的所有行，每行作为一个元素
   * @throws IOException 读取过程中发生 IO 错误时抛出
   */
  public static List<String> readAllLines(Path path) throws IOException {
    return readAllLines(path, StandardCharsets.UTF_8);
  }

  /**
   * 读取文件全部行到 {@link List} 中，使用指定编码。
   *
   * @param path    文件路径
   * @param charset 字符编码
   * @return 文件中的所有行
   * @throws IOException 读取过程中发生 IO 错误时抛出
   */
  public static List<String> readAllLines(Path path, Charset charset) throws IOException {
    if (path == null) {
      return List.of();
    }
    return Files.readAllLines(path, charset);
  }

  /**
   * 将整个文件内容读取为字符串，使用 UTF-8 编码。
   *
   * @param path 文件路径
   * @return 文件的全部内容
   * @throws IOException 读取过程中发生 IO 错误时抛出
   */
  public static String readString(Path path) throws IOException {
    return readString(path, StandardCharsets.UTF_8);
  }

  /**
   * 将整个文件内容读取为字符串，使用指定编码。
   *
   * @param path    文件路径
   * @param charset 字符编码
   * @return 文件的全部内容
   * @throws IOException 读取过程中发生 IO 错误时抛出
   */
  public static String readString(Path path, Charset charset) throws IOException {
    if (path == null) {
      return "";
    }
    return Files.readString(path, charset);
  }

  /**
   * 将字符串写入文件，使用 UTF-8 编码（覆写模式）。
   *
   * @param path    文件路径
   * @param content 字符串内容
   * @throws IOException 写入过程中发生 IO 错误时抛出
   */
  public static void writeString(Path path, String content) throws IOException {
    writeString(path, content, StandardCharsets.UTF_8, false);
  }

  /**
   * 将字符串写入文件，使用指定编码和写入模式。
   *
   * @param path    文件路径
   * @param content 字符串内容
   * @param charset 字符编码
   * @param append  {@code true} 为追加模式，{@code false} 为覆写模式
   * @throws IOException 写入过程中发生 IO 错误时抛出
   */
  public static void writeString(Path path, String content, Charset charset, boolean append) throws IOException {
    if (path == null || content == null) {
      return;
    }
    OpenOption[] options = append
        ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
        : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
    Files.writeString(path, content, charset, options);
  }

  /**
   * 将多行文本写入文件，使用 UTF-8 编码（覆写模式）。
   *
   * @param path  文件路径
   * @param lines 文本行集合
   * @throws IOException 写入过程中发生 IO 错误时抛出
   */
  public static void writeLines(Path path, List<String> lines) throws IOException {
    writeLines(path, lines, StandardCharsets.UTF_8, false);
  }

  /**
   * 将多行文本写入文件，使用指定编码和写入模式。
   *
   * @param path    文件路径
   * @param lines   文本行集合
   * @param charset 字符编码
   * @param append  {@code true} 为追加模式，{@code false} 为覆写模式
   * @throws IOException 写入过程中发生 IO 错误时抛出
   */
  public static void writeLines(Path path, List<String> lines, Charset charset, boolean append) throws IOException {
    if (path == null || lines == null) {
      return;
    }
    OpenOption[] options = append
        ? new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND}
        : new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
    Files.write(path, lines, charset, options);
  }
}
