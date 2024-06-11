package cn.labzen.tool.definition

import cn.labzen.tool.definition.FileTypes.*

/**
 * 文件扩展名
 */
@Suppress("unused")
enum class FileExtensions(val kinds: FileTypes, val fileExtension: String) {
  NOT_A_FILE(OTHER, ""),
  TEXT_TXT(TEXT, ".txt"),
  TEXT_MD(TEXT, ".md"),

  IMAGE_JPG(IMAGE, ".jpg"),
  IMAGE_JPEG(IMAGE, ".jpeg"),
  IMAGE_PNG(IMAGE, ".png"),
  IMAGE_BMP(IMAGE, ".bmp"),
  IMAGE_GIF(IMAGE, ".gif"),
  IMAGE_ICO(IMAGE, ".ico"),
  IMAGE_SVG(IMAGE, ".svg"),

  CODE_JAVA(CODE, ".java"),
  CODE_KOTLIN(CODE, ".kotlin"),
  CODE_XML(CODE, ".xml"),
  CODE_YML(CODE, ".yml"),
  CODE_YAML(CODE, ".yaml"),
  CODE_JSON(CODE, ".json"),
  CODE_PROPERTIES(CODE, ".properties"),
  CODE_INI(CODE, ".ini"),
  CODE_HTML(CODE, ".html"),
  CODE_JS(CODE, ".js"),
  CODE_CSS(CODE, ".css"),
  CODE_C(CODE, ".c"),
  CODE_CPP(CODE, ".cpp"),
  CODE_H(CODE, ".h"),
  CODE_PYTHON(CODE, ".python"),
  CODE_PHP(CODE, ".php"),

  SHELL_BAT(SHELL, ".bat"),
  SHELL_SH(SHELL, ".sh"),

  EXECUTABLE_EXE(EXECUTABLE, ".exe"),

  OFFICE_XLS(OFFICE, ".xls"),
  OFFICE_XLSX(OFFICE, ".xlsx"),
  OFFICE_DOC(OFFICE, ".doc"),
  OFFICE_DOCX(OFFICE, ".docx"),
  OFFICE_PPT(OFFICE, ".ppt"),
  OFFICE_PPTX(OFFICE, ".pptx"),
  OFFICE_PDF(OFFICE, ".pdf"),

  AUDIO_MP3(AUDIO, ".mp3"),
  AUDIO_WAV(AUDIO, ".wav"),
  AUDIO_WMA(AUDIO, ".wma"),
  AUDIO_MIDI(AUDIO, ".midi"),
  AUDIO_OGG(AUDIO, ".ogg"),
  AUDIO_AAC(AUDIO, ".aac"),

  VIDEO_AVI(VIDEO, ".avi"),
  VIDEO_MP4(VIDEO, ".mp4"),
  VIDEO_WMV(VIDEO, ".wmv"),
  VIDEO_MPG(VIDEO, ".mpg"),
  VIDEO_MOV(VIDEO, ".mov"),
  VIDEO_RM(VIDEO, ".rmvb"),
  VIDEO_SWF(VIDEO, ".swf"),

  COMPRESSED_ZIP(COMPRESSED, ".zip"),
  COMPRESSED_RAR(COMPRESSED, ".rar"),
  COMPRESSED_7Z(COMPRESSED, ".7z"),
  COMPRESSED_JAR(COMPRESSED, ".jar"),
  COMPRESSED_TAR_GZ(COMPRESSED, ".tar.gz"),
  COMPRESSED_TAR_BZ2(COMPRESSED, ".tar.bz2"),
  COMPRESSED_CAB(COMPRESSED, ".cab"),
  COMPRESSED_LIB(COMPRESSED, ".lib"),
  COMPRESSED_ISO(COMPRESSED, ".iso"),

  CRYPTO_CRT(CRYPTO, ".crt"),
  CRYPTO_CER(CRYPTO, ".cer"),
  CRYPTO_PEM(CRYPTO, ".pem"),
  CRYPTO_P12(CRYPTO, ".p12"),
  CRYPTO_JKS(CRYPTO, ".jks"),

  BINARY_DLL(BINARY, ".dll"),
  BINARY_DAT(BINARY, ".dat"),
  BINARY_SO(BINARY, ".so"),

  FONT_TTF(FONT, ".ttf"),
  FONT_OTF(FONT, ".otf"),
  FONT_EOT(FONT, ".eot"),
  FONT_WOFF(FONT, ".woff"),
  FONT_WOFF2(FONT, ".woff2"),
}
