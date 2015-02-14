package org.bm.storesystem

import org.bm.storesystem.{StoreSystemConfigReader => r}

/**
 * .
 * @author Baptiste Morin
 */
trait Config {
  def storagePath: String = r.config.getProperty("storage.path", "./storage")
  def hashAlgorithm: String = r.config.getProperty("hash.algorithm", "MD5")
  def readBufferSize: Int = r.config.getProperty("read.buffer.size", "2048").toInt
  def directoryHashDigitsNumber: Long = r.config.getProperty("directory.hash.digits.number", "2").toLong
}
