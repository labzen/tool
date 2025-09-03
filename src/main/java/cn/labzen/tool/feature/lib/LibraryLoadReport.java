package cn.labzen.tool.feature.lib;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryLoadReport {

  private Map<String, LibraryLoadStatus> statusMap = new HashMap<>();

  void loaded(File file) {
    statusMap.putIfAbsent(file.getAbsolutePath(), new LibraryLoadStatus(file, true, null));
  }

  void failed(File file, Throwable throwable) {
    statusMap.putIfAbsent(file.getAbsolutePath(), new LibraryLoadStatus(file, false, throwable.getMessage()));
  }

  public int total() {
    return statusMap.size();
  }

  public List<String> loadedFilePaths() {
    return filePaths(true);
  }

  public List<String> failedFilePaths() {
    return filePaths(false);
  }

  private List<String> filePaths(boolean loaded) {
    return statusMap.entrySet()
                    .stream()
                    .filter(entry -> loaded == entry.getValue().loaded())
                    .map(Map.Entry::getKey)
                    .toList();
  }

  List<File> failureFiles() {
    return statusMap.values().stream().filter(status -> !status.loaded()).map(LibraryLoadStatus::file).toList();
  }
}
