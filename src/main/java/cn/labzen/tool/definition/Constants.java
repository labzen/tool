package cn.labzen.tool.definition;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Constants {

  String DEFAULT_CHARSET_NAME = "UTF-8";
  Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  // projects package path
  String PACKAGE_ROOT = "cn.labzen";
  String PACKAGE_META = PACKAGE_ROOT + ".meta";
  String PACKAGE_LOGGER = PACKAGE_ROOT + ".logger";
  String PACKAGE_CELLS = PACKAGE_ROOT + ".cells";
  String PACKAGE_TOOL = PACKAGE_ROOT + ".tool";
  String PACKAGE_NETWORK = PACKAGE_ROOT + ".network";
  String PACKAGE_ALGORITHM = PACKAGE_ROOT + ".algorithm";
  String PACKAGE_SPRING = PACKAGE_ROOT + ".spring";
  String PACKAGE_PLUGIN = PACKAGE_ROOT + ".plugin";
  String PACKAGE_JAVAFX = PACKAGE_ROOT + ".javafx";
  String PACKAGE_CLI = PACKAGE_ROOT + ".cli";
  String PACKAGE_WEB = PACKAGE_ROOT + ".web";
  String PACKAGE_PARAGON = PACKAGE_ROOT + ".paragon";

  // separators and symbols
  String SEPARATOR_COMMAS = ",";
  String SEPARATOR_DOT = ".";
  String SEPARATOR_UNDERLINE = "_";
  String SEPARATOR_STRIKE = "-";
  String SEPARATOR_COLON = ":";
  String SEPARATOR_SLASH = "/";
  String SEPARATOR_BACKSLASH = "\\";

  String SYMBOL_BANG = "!";
  String SYMBOL_AT = "@";
  String SYMBOL_POUND = "#";
  String SYMBOL_DOLLAR = "$";
  String SYMBOL_PERCENT = "%";
  String SYMBOL_POWER = "^";
  String SYMBOL_AND = "&";
  String SYMBOL_ASTERISK = "*";
  String SYMBOL_BRACKET_LEFT = "(";
  String SYMBOL_BRACKET_RIGHT = ")";
  String SYMBOL_MINUS = "-";
  String SYMBOL_PLUS = "+";
  String SYMBOL_UNDERLINE = "_";
  String SYMBOL_SQUARE_BRACKET_LEFT = "[";
  String SYMBOL_SQUARE_BRACKET_RIGHT = "]";
  String SYMBOL_BRACE_LEFT = "{";
  String SYMBOL_BRACE_RIGHT = "}";
  String SYMBOL_EQUAL = "=";
  String SYMBOL_CR = "\\r";
  String SYMBOL_BR = "\\n";
  String SYMBOL_TAB = "\\t";

  // date and times
  String PATTERN_OF_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
  String PATTERN_OF_DATE_TIME_MILL = "yyyy-MM-dd HH:mm:ss.SSS";
  String PATTERN_OF_DATE = "yyyy-MM-dd";
  String PATTERN_OF_DATE_WEEK = "yyyy-MM-dd E";
  String PATTERN_OF_TIME = "HH:mm:ss";
  String PATTERN_OF_TIME_MILL = "HH:mm:ss.SSS";
  String PATTERN_CN_OF_DATE_TIME = "yyyy年MM月dd日 HH时mm分ss秒";
  String PATTERN_CN_OF_DATE_TIME_MILL = "yyyy年MM月dd日 HH时mm分ss秒.SSS毫秒";
  String PATTERN_CN_OF_DATE = "yyyy年MM月dd日";
  String PATTERN_CN_OF_TIME = "HH时mm分ss秒";
  String PATTERN_CN_OF_TIME_MILL = "HH时mm分ss秒.SSS毫秒";
}
