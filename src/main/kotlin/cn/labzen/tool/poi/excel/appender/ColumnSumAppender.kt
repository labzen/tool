package cn.labzen.tool.poi.excel.appender

import cn.labzen.tool.util.Objects

class ColumnSumAppender : ColumnAppender {

  override fun calculate(cellsData: List<Any?>): String =
    cellsData.mapNotNull {
      Objects.canBeLong(it?.toString())
    }.sum().toString()
}
