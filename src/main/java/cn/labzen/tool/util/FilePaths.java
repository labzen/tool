package cn.labzen.tool.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 跨平台文件路径工具类，提供路径拼接、文件名/扩展名提取、父路径获取、分隔符转换等功能。
 * 所有方法均不依赖操作系统当前的分隔符，可安全用于跨平台路径处理。
 */
public final class FilePaths {

  private FilePaths() {
  }

  /**
   * 跨平台路径拼接，自动使用当前操作系统的路径分隔符。
   *
   * <pre>{@code
   * FilePaths.combine("C:", "windows", "system32")  // Windows: "C:\windows\system32"，Unix: "C:/windows/system32"
   * FilePaths.combine("/usr", "local", "bin")        // "/usr/local/bin"
   * }</pre>
   *
   * @param paths 路径碎片，空参数时返回空字符串
   * @return 拼接后的路径字符串
   */
  public static String combine(String... paths) {
    if (paths == null || paths.length == 0) {
      return "";
    }
    if (paths.length == 1) {
      return paths[0];
    }
    Path result = Paths.get(paths[0], Arrays.copyOfRange(paths, 1, paths.length));
    return result.toString();
  }

  /**
   * 基于已有的 {@link Path} 根路径，追加路径碎片进行拼接。
   *
   * <pre>{@code
   * Path root = Paths.get("/usr");
   * FilePaths.combine(root, "local", "bin")  // "/usr/local/bin"
   * }</pre>
   *
   * @param root  根路径，不可为 null
   * @param paths 路径碎片
   * @return 拼接后的 {@link Path} 对象
   */
  public static Path combine(Path root, String... paths) {
    if (paths == null || paths.length == 0 || root == null) {
      return root;
    }
    Path result = root;
    for (String p : paths) {
      if (p != null && !p.isEmpty()) {
        result = result.resolve(p);
      }
    }
    return result;
  }

  /**
   * 获取文件扩展名，不含点号。
   *
   * <pre>{@code
   * FilePaths.extension("document.pdf")      // "pdf"
   * FilePaths.extension("/usr/local/bin")    // ""
   * FilePaths.extension("archive.tar.gz")    // "gz"
   * }</pre>
   *
   * @param path 文件路径
   * @return 文件扩展名（小写），无扩展名时返回空字符串
   */
  public static String extension(String path) {
    if (Strings.isEmpty(path)) {
      return "";
    }
    int dotIndex = path.lastIndexOf('.');
    int sepIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
    if (dotIndex <= sepIndex) {
      return "";
    }
    return path.substring(dotIndex + 1).toLowerCase();
  }

  /**
   * 获取文件扩展名，不含点号。
   *
   * @param path 文件 {@link Path} 对象
   * @return 文件扩展名（小写），无扩展名时返回空字符串
   * @see #extension(String)
   */
  public static String extension(Path path) {
    return path == null ? "" : extension(path.toString());
  }

  /**
   * 获取文件扩展名，不含点号。
   *
   * @param file 文件对象
   * @return 文件扩展名（小写），无扩展名时返回空字符串
   * @see #extension(String)
   */
  public static String extension(File file) {
    return file == null ? "" : extension(file.getName());
  }

  /**
   * 获取文件名（不含扩展名）。
   *
   * <pre>{@code
   * FilePaths.filename("/usr/local/readme.txt")   // "readme"
   * FilePaths.filename("C:\\temp\\data.csv")      // "data"
   * FilePaths.filename("archive.tar.gz")           // "archive.tar"
   * }</pre>
   *
   * @param path 文件路径
   * @return 不带扩展名的文件名
   */
  public static String filename(String path) {
    if (Strings.isEmpty(path)) {
      return "";
    }
    String full = fullFilename(path);
    int dotIndex = full.lastIndexOf('.');
    return dotIndex > 0 ? full.substring(0, dotIndex) : full;
  }

  /**
   * 获取文件名（不含扩展名）。
   *
   * @param path 文件 {@link Path} 对象
   * @return 不带扩展名的文件名
   * @see #filename(String)
   */
  public static String filename(Path path) {
    return path == null ? "" : filename(path.toString());
  }

  /**
   * 获取文件名（不含扩展名）。
   *
   * @param file 文件对象
   * @return 不带扩展名的文件名
   * @see #filename(String)
   */
  public static String filename(File file) {
    return file == null ? "" : filename(file.getName());
  }

  /**
   * 获取完整文件名（含扩展名）。
   *
   * <pre>{@code
   * FilePaths.fullFilename("/usr/local/readme.txt")   // "readme.txt"
   * FilePaths.fullFilename("C:\\temp\\data.csv")      // "data.csv"
   * }</pre>
   *
   * @param path 文件路径
   * @return 含扩展名的完整文件名
   */
  public static String fullFilename(String path) {
    if (Strings.isEmpty(path)) {
      return "";
    }
    int sepIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
    return path.substring(sepIndex + 1);
  }

  /**
   * 获取完整文件名（含扩展名）。
   *
   * @param path 文件 {@link Path} 对象
   * @return 含扩展名的完整文件名
   * @see #fullFilename(String)
   */
  public static String fullFilename(Path path) {
    return path == null ? "" : (path.getFileName() != null ? path.getFileName().toString() : "");
  }

  /**
   * 获取完整文件名（含扩展名）。
   *
   * @param file 文件对象
   * @return 含扩展名的完整文件名
   * @see #fullFilename(String)
   */
  public static String fullFilename(File file) {
    return file == null ? "" : file.getName();
  }

  /**
   * 获取文件的父路径（向上一级）。
   *
   * <pre>{@code
   * FilePaths.parentPath("/usr/local/bin")   // "/usr/local"
   * FilePaths.parentPath("C:\\temp\\data")   // "C:\\temp"
   * }</pre>
   *
   * @param path 文件路径
   * @return 父路径字符串，已到根目录时返回 {@code null}
   */
  public static String parentPath(String path) {
    return parentPath(path, 1);
  }

  /**
   * 获取文件的父路径（向上指定层级）。
   *
   * <pre>{@code
   * FilePaths.parentPath("/usr/local/bin", 2)   // "/usr"
   * }</pre>
   *
   * @param path  文件路径
   * @param level 向上层级，必须大于 0
   * @return 父路径字符串，层级超出根目录时返回 {@code null}
   */
  public static String parentPath(String path, int level) {
    if (Strings.isEmpty(path) || level <= 0) {
      return path;
    }
    File file = new File(path);
    for (int i = 0; i < level; i++) {
      String parent = file.getParent();
      if (parent == null) {
        return null;
      }
      file = new File(parent);
    }
    return file.getPath();
  }

  /**
   * 获取文件的父路径（向上一级）。
   *
   * @param path 文件 {@link Path} 对象
   * @return 父 {@link Path}，已到根目录时返回 {@code null}
   */
  public static Path parentPath(Path path) {
    return parentPath(path, 1);
  }

  /**
   * 获取文件的父路径（向上指定层级）。
   *
   * @param path  文件 {@link Path} 对象
   * @param level 向上层级，必须大于 0
   * @return 父 {@link Path}，层级超出根目录时返回 {@code null}
   */
  public static Path parentPath(Path path, int level) {
    if (path == null || level <= 0) {
      return path;
    }
    Path result = path;
    for (int i = 0; i < level; i++) {
      result = result.getParent();
      if (result == null) {
        return null;
      }
    }
    return result;
  }

  /**
   * 获取文件的父路径（向上一级）。
   *
   * @param file 文件对象
   * @return 父路径字符串，已到根目录时返回 {@code null}
   */
  public static String parentPath(File file) {
    return parentPath(file, 1);
  }

  /**
   * 获取文件的父路径（向上指定层级）。
   *
   * @param file  文件对象
   * @param level 向上层级，必须大于 0
   * @return 父路径字符串，层级超出根目录时返回 {@code null}
   */
  public static String parentPath(File file, int level) {
    if (file == null || level <= 0) {
      return file == null ? null : file.getPath();
    }
    File current = file;
    for (int i = 0; i < level; i++) {
      String parent = current.getParent();
      if (parent == null) {
        return null;
      }
      current = new File(parent);
    }
    return current.getPath();
  }

  /**
   * 判断路径是否使用斜杠 {@code /} 作为分隔符（即 Unix/Linux/macOS 风格）。
   *
   * <pre>{@code
   * FilePaths.isSlashSeparated("/usr/local")    // true
   * FilePaths.isSlashSeparated("C:\\temp")      // false
   * }</pre>
   *
   * @param path 路径字符串
   * @return 路径中不包含反斜杠 {@code \\} 时为 {@code true}
   */
  public static boolean isSlashSeparated(String path) {
    if (Strings.isEmpty(path)) {
      return true;
    }
    return path.indexOf('\\') < 0;
  }

  /**
   * 将路径中的所有反斜杠 {@code \\} 替换为斜杠 {@code /}，转换为 Unix 风格路径。
   *
   * <pre>{@code
   * FilePaths.convert2SlashPath("C:\\temp\\data")   // "C:/temp/data"
   * FilePaths.convert2SlashPath("/usr/local")       // "/usr/local"（不变）
   * }</pre>
   *
   * @param path 原始路径
   * @return 斜杠风格的路径字符串
   */
  public static String convert2SlashPath(String path) {
    if (Strings.isEmpty(path)) {
      return path;
    }
    return path.replace('\\', '/');
  }
}
