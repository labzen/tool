package cn.labzen.tool.poi.excel.appender

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors

data class AppendData(
  val title: String?,
  val data: String?,
  val alignment: HorizontalAlignment = HorizontalAlignment.LEFT,
  val background: IndexedColors = IndexedColors.WHITE,
  val fontColor: IndexedColors = IndexedColors.BLACK,
)
