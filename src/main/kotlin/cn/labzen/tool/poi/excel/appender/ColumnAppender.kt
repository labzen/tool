package cn.labzen.tool.poi.excel.appender

interface ColumnAppender {

  fun calculate(cellsData: List<*>): AppendData
}
