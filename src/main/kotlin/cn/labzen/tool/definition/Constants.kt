package cn.labzen.tool.definition

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class Constants private constructor() {

  companion object {
    // encoding
    const val DEFAULT_CHARSET_NAME = "UTF-8"

    @JvmField
    val DEFAULT_CHARSET: Charset = StandardCharsets.UTF_8

    // projects package path
    const val PACKAGE_ROOT = "cn.labzen"
    const val PACKAGE_LABZEN = "cn.labzen"
    const val PACKAGE_META = "$PACKAGE_ROOT.meta"
    const val PACKAGE_LOGGER = "$PACKAGE_ROOT.logger"
    const val PACKAGE_CELLS = "$PACKAGE_ROOT.cells"
    const val PACKAGE_TOOL = "$PACKAGE_ROOT.tool"
    const val PACKAGE_NETWORK = "$PACKAGE_ROOT.network"
    const val PACKAGE_ALGORITHM = "$PACKAGE_ROOT.algorithm"
    const val PACKAGE_SPRING = "$PACKAGE_ROOT.spring"
    const val PACKAGE_PLUGIN = "$PACKAGE_ROOT.plugin"
    const val PACKAGE_JAVAFX = "$PACKAGE_ROOT.javafx"
    const val PACKAGE_CLI = "$PACKAGE_ROOT.cli"
    const val PACKAGE_WEB = "$PACKAGE_ROOT.web"
    const val PACKAGE_PARAGON = "$PACKAGE_ROOT.paragon"

    // separators and symbols
    const val SEPARATOR_COMMAS = ","
    const val SEPARATOR_DOT = "."
    const val SEPARATOR_UNDERLINE = "_"
    const val SEPARATOR_STRIKE = "-"
    const val SEPARATOR_COLON = ":"
    const val SEPARATOR_SLASH = "/"
    const val SEPARATOR_BACKSLASH = "\\"

    const val SYMBOL_BANG = "!"
    const val SYMBOL_AT = "@"
    const val SYMBOL_POUND = "#"
    const val SYMBOL_DOLLAR = "$"
    const val SYMBOL_PERCENT = "%"
    const val SYMBOL_POWER = "^"
    const val SYMBOL_AND = "&"
    const val SYMBOL_ASTERISK = "*"
    const val SYMBOL_BRACKET_LEFT = "("
    const val SYMBOL_BRACKET_RIGHT = ")"
    const val SYMBOL_MINUS = "-"
    const val SYMBOL_PLUS = "+"
    const val SYMBOL_UNDERLINE = "_"
    const val SYMBOL_SQUARE_BRACKET_LEFT = "["
    const val SYMBOL_SQUARE_BRACKET_RIGHT = "]"
    const val SYMBOL_BRACE_LEFT = "{"
    const val SYMBOL_BRACE_RIGHT = "}"
    const val SYMBOL_EQUAL = "="
    const val SYMBOL_CR = "\\r"
    const val SYMBOL_BR = "\\n"
    const val SYMBOL_TAB = "\\t"

    // date and times
    const val PATTERN_OF_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_OF_DATE_TIME_MILL = "yyyy-MM-dd HH:mm:ss.SSS"
    const val PATTERN_OF_DATE = "yyyy-MM-dd"
    const val PATTERN_OF_DATE_WEEK = "yyyy-MM-dd E"
    const val PATTERN_OF_TIME = "HH:mm:ss"
    const val PATTERN_OF_TIME_MILL = "HH:mm:ss.SSS"

    const val PATTERN_CN_OF_DATE_TIME = "yyyy年MM月dd日 HH时mm分ss秒"
    const val PATTERN_CN_OF_DATE_TIME_MILL = "yyyy年MM月dd日 HH时mm分ss秒.SSS毫秒"
    const val PATTERN_CN_OF_DATE = "yyyy年MM月dd日"
    const val PATTERN_CN_OF_TIME = "HH时mm分ss秒"
    const val PATTERN_CN_OF_TIME_MILL = "HH时mm分ss秒.SSS毫秒"
  }
}
