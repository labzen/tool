package cn.labzen.tool.feature.lib

import java.io.File

data class DynamicLinkLibraryLoadStatus(val file: File) {

  var loaded: Boolean = false
  var failedCause: String = ""
}
