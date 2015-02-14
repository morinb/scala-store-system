package org.bm.storesystem

import java.io.IOException
import java.nio.file.{FileSystems, Files, Path}

import org.bm.storesystem.{StoreSystemConfigReader => r}

/**
 * .
 * @author Baptiste Morin
 */
trait Config {
  def storagePath: String = r.config.getProperty("storage.path", "./storesystem/storage")

  def indexPath: String = r.config.getProperty("index.path", "./storesystem/index")

  def hashAlgorithm: String = r.config.getProperty("hash.algorithm", "MD5")

  def readBufferSize: Int = r.config.getProperty("read.buffer.size", "2048").toInt

  def directoryHashDigitsNumber: Int = r.config.getProperty("directory.hash.digits.number", "2").toInt

  def ensureDirectoryExists(path: String): Path = ensureDirectoryExists(FileSystems.getDefault.getPath(path))

  def ensureDirectoryExists(path: Path): Path = {
    if (!Files.isDirectory(path)) {
      try {
        Files.createDirectories(path)
      } catch {
        case e: IOException => e.printStackTrace()
      }
    }

    path
  }
}
