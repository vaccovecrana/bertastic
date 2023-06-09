package io.vacco.bertastic;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.*;

public class FileIO {

  public static void unzip(URL zipUrl, File outDir) throws IOException {
    try (var inputStream = zipUrl.openStream();
         var zipInputStream = new ZipInputStream(inputStream)) {
      var buffer = new byte[1024];
      ZipEntry entry;
      while ((entry = zipInputStream.getNextEntry()) != null) {
        var fileName = entry.getName();
        var outputFile = new File(outDir, fileName);
        if (entry.isDirectory()) {
          Files.createDirectories(outputFile.toPath());
        } else {
          Files.createDirectories(outputFile.getParentFile().toPath());
          try (var outputStream = new FileOutputStream(outputFile)) {
            int length;
            while ((length = zipInputStream.read(buffer)) > 0) {
              outputStream.write(buffer, 0, length);
            }
          }
        }
        zipInputStream.closeEntry();
      }
    }
  }

  public static File newTempDir() {
    try {
      return Files.createTempDirectory("easy-bert-").toFile();
    } catch (Exception e) {
      throw new IllegalStateException("Unable to create temp directory", e);
    }
  }

}
