package cn.labzen.tool.definition;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Constants {

  private Constants() {
  }

  public static final String DEFAULT_CHARSET_NAME = "UTF-8";
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  // projects package path
  public static final String PACKAGE_ROOT = "cn.labzen";
  public static final String PACKAGE_META = PACKAGE_ROOT + ".meta";
  public static final String PACKAGE_LOGGER = PACKAGE_ROOT + ".logger";
  public static final String PACKAGE_CELLS = PACKAGE_ROOT + ".cells";
  public static final String PACKAGE_TOOL = PACKAGE_ROOT + ".tool";
  public static final String PACKAGE_NETWORK = PACKAGE_ROOT + ".network";
  public static final String PACKAGE_ALGORITHM = PACKAGE_ROOT + ".algorithm";
  public static final String PACKAGE_SPRING = PACKAGE_ROOT + ".spring";
  public static final String PACKAGE_PLUGIN = PACKAGE_ROOT + ".plugin";
  public static final String PACKAGE_JAVAFX = PACKAGE_ROOT + ".javafx";
  public static final String PACKAGE_CLI = PACKAGE_ROOT + ".cli";
  public static final String PACKAGE_WEB = PACKAGE_ROOT + ".web";
  public static final String PACKAGE_PARAGON = PACKAGE_ROOT + ".paragon";

  // separators and symbols
  public static final String SEPARATOR_COMMAS = ",";
  public static final String SEPARATOR_DOT = ".";
  public static final String SEPARATOR_UNDERLINE = "_";
  public static final String SEPARATOR_STRIKE = "-";
  public static final String SEPARATOR_COLON = ":";
  public static final String SEPARATOR_SLASH = "/";
  public static final String SEPARATOR_BACKSLASH = "\\";

  public static final String SYMBOL_BANG = "!";
  public static final String SYMBOL_AT = "@";
  public static final String SYMBOL_POUND = "#";
  public static final String SYMBOL_DOLLAR = "$";
  public static final String SYMBOL_PERCENT = "%";
  public static final String SYMBOL_POWER = "^";
  public static final String SYMBOL_AND = "&";
  public static final String SYMBOL_ASTERISK = "*";
  public static final String SYMBOL_BRACKET_LEFT = "(";
  public static final String SYMBOL_BRACKET_RIGHT = ")";
  public static final String SYMBOL_MINUS = "-";
  public static final String SYMBOL_PLUS = "+";
  public static final String SYMBOL_UNDERLINE = "_";
  public static final String SYMBOL_SQUARE_BRACKET_LEFT = "[";
  public static final String SYMBOL_SQUARE_BRACKET_RIGHT = "]";
  public static final String SYMBOL_BRACE_LEFT = "{";
  public static final String SYMBOL_BRACE_RIGHT = "}";
  public static final String SYMBOL_EQUAL = "=";
  public static final String SYMBOL_CR = "\\r";
  public static final String SYMBOL_BR = "\\n";
  public static final String SYMBOL_TAB = "\\t";

  // date and times
  public static final String PATTERN_OF_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
  public static final String PATTERN_OF_DATE_TIME_MILL = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String PATTERN_OF_DATE = "yyyy-MM-dd";
  public static final String PATTERN_OF_DATE_WEEK = "yyyy-MM-dd E";
  public static final String PATTERN_OF_TIME = "HH:mm:ss";
  public static final String PATTERN_OF_TIME_MILL = "HH:mm:ss.SSS";
  public static final String PATTERN_CN_OF_DATE_TIME = "yyyy年MM月dd日 HH时mm分ss秒";
  public static final String PATTERN_CN_OF_DATE_TIME_MILL = "yyyy年MM月dd日 HH时mm分ss秒.SSS毫秒";
  public static final String PATTERN_CN_OF_DATE = "yyyy年MM月dd日";
  public static final String PATTERN_CN_OF_TIME = "HH时mm分ss秒";
  public static final String PATTERN_CN_OF_TIME_MILL = "HH时mm分ss秒.SSS毫秒";
}
