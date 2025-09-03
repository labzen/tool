package cn.labzen.tool.util;

import java.io.File;
import java.nio.file.FileSystems;

public final class Systems {

  private static final OS CUR_OS;

  static {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win")) {
      CUR_OS = OS.WINDOWS;
    } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
      boolean android = false;
      try {
        Class.forName("android.os.Build");
        android = true;
      } catch (ClassNotFoundException e) {
        // do nothing
      }
      CUR_OS = android ? OS.ANDROID : OS.LINUX;
    } else if (osName.contains("mac")) {
      CUR_OS = OS.MAC;
    } else if (osName.contains("sunos")) {
      CUR_OS = OS.SOLARIS;
    } else {
      CUR_OS = OS.UNKNOWN;
    }
  }

  private Systems() {
  }

  public static OS getOS() {
    return CUR_OS;
  }

  public static int getOSArch() {
    return System.getProperty("os.arch").contains("64") ? 64 : 32;
  }

  public static String javaVersion() {
    return System.getProperty("java.version");
  }

  public static String javaVendor() {
    return System.getProperty("java.vendor");
  }

  public static String javaHome() {
    return System.getProperty("java.home");
  }

  public static String javaClasspath() {
    return System.getProperty("java.class.path");
  }

  public static String osVersion() {
    return System.getProperty("os.version");
  }

  public static String fileSeparator() {
    return FileSystems.getDefault().getSeparator();
  }

  public static String pathSeparator() {
    return File.pathSeparator;
  }

  public static String lineSeparator() {
    return System.lineSeparator();
  }

  public static String userName() {
    return System.getProperty("user.name");
  }

  public static String userHome() {
    return System.getProperty("user.home");
  }

  public static String userDir() {
    return System.getProperty("user.dir");
  }

  public enum OS {
    WINDOWS, LINUX, MAC, SOLARIS, ANDROID, UNKNOWN
  }
}
