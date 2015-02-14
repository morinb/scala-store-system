package org.bm.storesystem.impl

import java.io.{FileInputStream, IOException, InputStream}
import java.nio.file.{FileSystems, Files, Path, StandardCopyOption}
import java.security.NoSuchAlgorithmException
import java.util.Properties

import org.apache.lucene.document.Document
import org.bm.storesystem._
import org.bm.storesystem.exception.MultipleResultException

/**
 * .
 * @author Baptiste Morin
 */
class FileSystemStoreSystem extends StoreSystem with Indexing with Config with HexHashUtils {
  val pathStorage = ensureDirectoryExists(FileSystems.getDefault.getPath(storagePath))
  val dataPath = ensureDirectoryExists(FileSystems.getDefault.getPath(storagePath, "data"))

  override def store(source: InputStream, filename: String): Option[Path] = {
    val bufferSize = readBufferSize
    try {
      val firstPart = digest(filename, bufferSize)(hashAlgorithm).substring(0, directoryHashDigitsNumber)
      val sb: StringBuilder = new StringBuilder(filename)
      while (sb.length < 6) {
        sb += '_'
      }
      val directory = sb.substring(0, 6).toUpperCase
      val whereToStore = ensureDirectoryExists(FileSystems.getDefault.getPath(storagePath, "data", directory, firstPart))

      val filePlace: Path = FileSystems.getDefault.getPath(storagePath, "data", directory, firstPart, filename)

      // Delete old version of file
      Files.deleteIfExists(filePlace)
      Files.createFile(filePlace)
      Files.copy(source, filePlace, StandardCopyOption.REPLACE_EXISTING)

      index(filename, filePlace.toString)
      return Some(filePlace)
    } catch {
      case e1: NumberFormatException => e1.printStackTrace()
      case e2: NoSuchAlgorithmException => e2.printStackTrace()
      case e3: IOException => e3.printStackTrace()
    }


    None
  }

  override def serve(filename: String): InputStream = {
    val hits = search(filename)

    if (hits.size != 1) {
      throw new MultipleResultException(s"${hits.size} result found for $filename. Please be more accurate in your query.")
    }

    val doc: Document = searcher.doc(hits(0).doc)

    new FileInputStream(doc.get("filepath"))
  }

  override def config: Properties = StoreSystemConfigReader.config

  override def close(): Unit = {
    reader.close()
  }
}
