package cn.labzen.tool.meta

import cn.labzen.meta.component.LabzenComponent

class ToolMeta : LabzenComponent {

  override fun description(): String =
    "Labzen.Tool"

  override fun mark(): String =
    "cn.labzen.tool"

  override fun packageBased(): String =
    "工具包，包含最基本的包依赖集合，并提供普适基础工具与底层功能"
}
