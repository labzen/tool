package cn.labzen.tool.feature.lib;

import java.io.File;

public record LibraryLoadStatus(File file, boolean loaded, String failedCause) {

}
