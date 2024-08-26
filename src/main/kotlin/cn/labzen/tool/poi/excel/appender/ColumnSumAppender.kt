package cn.labzen.tool.poi.excel.appender

import cn.labzen.tool.util.Objects

class ColumnSumAppender(private val title: String? = null) : ColumnAppender {

  override fun calculate(cellsData: List<Any?>): AppendData =
    AppendData(title, cellsData.mapNotNull {
      Objects.canBeLong(it?.toString())
    }.sum().toString())
}
