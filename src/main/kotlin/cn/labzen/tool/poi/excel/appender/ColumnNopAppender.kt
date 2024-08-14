package cn.labzen.tool.poi.excel.appender

class ColumnNopAppender : ColumnAppender {

  override fun calculate(cellsData: List<*>): String = ""
}
