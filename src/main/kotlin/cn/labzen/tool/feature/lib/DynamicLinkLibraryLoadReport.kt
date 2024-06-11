package cn.labzen.tool.feature.lib

import java.io.File

class DynamicLinkLibraryLoadReport {

  private val files = mutableMapOf<String, DynamicLinkLibraryLoadStatus>()

  internal fun loaded(file: File) {
    files.getOrPut(file.absolutePath) {
      DynamicLinkLibraryLoadStatus(file)
    }.apply {
      loaded = true
      failedCause = ""
    }
  }

  internal fun failure(file: File, t: Throwable) {
    files.getOrPut(file.absolutePath) {
      DynamicLinkLibraryLoadStatus(file)
    }.apply {
      loaded = false
      failedCause = t.message ?: "unknown cause"
    }
  }

  fun totalNumber() =
    files.size

  fun loadedFilePaths(): List<String> =
    files.filter { it.value.loaded }.map { it.key }

  fun failureFilePaths(): Map<String, String> =
    files.filter { !it.value.loaded }.mapValues { it.value.failedCause }

  internal fun failureFiles(): List<File> =
    files.filter { !it.value.loaded }.values.map { it.file }
}
