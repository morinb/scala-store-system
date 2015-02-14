package org.bm.storesystem.impl

import java.io.{IOException, InputStream}
import java.nio.file.{StandardCopyOption, FileSystems, Files, Path}
import java.security.NoSuchAlgorithmException

import org.apache.lucene.search.ScoreDoc
import org.bm.storesystem.{Config, HexHashUtils, Indexing, StoreSystem}

import scala.io.Source

/**
 * .
 * @author Baptiste Morin
 */
class FileSystemStoreSystem extends StoreSystem with Indexing with Config with HexHashUtils {
  val pathStorage = FileSystems.getDefault.getPath(storagePath)
  ensurePathExists(pathStorage)
  val dataPath = FileSystems.getDefault.getPath(storagePath, "data")
  ensurePathExists(dataPath)

  private def ensurePathExists(path: Path): Unit = {
    if (!Files.isDirectory(path)) {
      try {
        Files.createDirectories(path)
      } catch {
        case e: IOException => e.printStackTrace()
      }
    }
  }


  override def store(is: InputStream, filename: String): Option[Path] = {
    val bufferSize = readBufferSize
    try {
      val firstPart = digest(filename, bufferSize)(hashAlgorithm)
      val sb: StringBuilder = new StringBuilder
      while (sb.length < 6) {
        sb += '_'
      }
      val directory = sb.substring(0, 6).toUpperCase
      val whereToStore = FileSystems.getDefault.getPath(storagePath, "data", directory, firstPart)
      ensurePathExists(whereToStore)

      val filePlace = FileSystems.getDefault.getPath(storagePath, "data", directory, firstPart, filename)

      // Delete old version of file
      Files.deleteIfExists(filePlace)
      Files.createFile(filePlace)
      Files.copy(is, filePlace, StandardCopyOption.REPLACE_EXISTING)

      index(filename)
      return Some(filePlace)
    } catch {
      case e1: NumberFormatException => e1.printStackTrace()
      case e2: NoSuchAlgorithmException => e2.printStackTrace()
      case e3: IOException => e3.printStackTrace()
    }


    None
  }

  override def serve(filename: String): InputStream = {
    ???
  }
}
