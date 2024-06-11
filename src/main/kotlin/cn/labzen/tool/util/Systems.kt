package cn.labzen.tool.util

import java.io.File
import java.nio.file.FileSystems
import java.util.*

object Systems {

  /**
   * 系统名称
   */
  @JvmStatic
  val osName: OS by lazy {
    val name = System.getProperty("os.name").lowercase(Locale.getDefault())

    when {
      name.contains("win") -> OS.WINDOWS
      name.contains("nix") || name.contains("nux") || name.contains("aix") ->
        try {
          Class.forName("android.os.Build")
          OS.ANDROID
        } catch (e: ClassNotFoundException) {
          OS.LINUX
        }

      name.contains("mac") -> OS.MAC
      name.contains("sunos") -> OS.SOLARIS
      else -> OS.UNKNOWN
    }
  }

  /**
   * JVM是多少位系统，返回只有32和64两个值
   */
  @JvmStatic
  val osArch: Int by lazy {
    val arch = System.getProperty("os.arch")
    if (arch.contains("64")) 64 else 32
  }


  // system properties
  @JvmField
  val javaVersion: String = System.getProperty("java.version")

  @JvmField
  val javaVendor: String = System.getProperty("java.vendor")

  @JvmField
  val javaHome: String = System.getProperty("java.home")

  @JvmField
  val javaClasspath: String = System.getProperty("java.class.path")

  @JvmField
  val osVersion: String = System.getProperty("os.version")

  @JvmField
  val fileSeparator: String = FileSystems.getDefault().separator

  @JvmField
  val pathSeparator: String = File.pathSeparator

  @JvmField
  val lineSeparator: String = System.lineSeparator()

  @JvmField
  val userName: String = System.getProperty("user.name")

  @JvmField
  val userHome: String = System.getProperty("user.home")

  @JvmField
  val userDir: String = System.getProperty("user.dir")

  enum class OS {
    WINDOWS, LINUX, MAC, SOLARIS, ANDROID, UNKNOWN
  }
}
