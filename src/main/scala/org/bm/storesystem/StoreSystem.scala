package org.bm.storesystem

import java.io.InputStream
import java.nio.file.Path
import java.util.Properties

import org.bm.storesystem.impl.FileSystemStoreSystem

/**
 * .
 * @author Baptiste Morin
 */
trait StoreSystem {
  def store(is: InputStream, filename: String): Option[Path]

  def serve(filename: String): InputStream

  def config: Properties

  def close(): Unit
}

object StoreSystem {
  def apply(): StoreSystem = new FileSystemStoreSystem
}