package cn.labzen.tool.definition

/**
 * 文件类别，对文件的一个大概功用区分
 */
@Suppress("unused")
enum class FileTypes {

  /**
   * 文本文件
   */
  TEXT,

  /**
   * 图片文件
   */
  IMAGE,

  /**
   * 音频文件
   */
  AUDIO,

  /**
   * 视频文件
   */
  VIDEO,

  /**
   * 源码文件
   */
  CODE,

  /**
   * 脚本文件
   */
  SHELL,

  /**
   * 可执行文件
   */
  EXECUTABLE,

  /**
   * 办公文档文件
   */
  OFFICE,

  /**
   * 压缩文件
   */
  COMPRESSED,

  /**
   * 加密/证书文件
   */
  CRYPTO,

  /**
   * 二进制文件
   */
  BINARY,

  /**
   * 字体文件
   */
  FONT,

  /**
   * 其他文件类别
   */
  OTHER
}
