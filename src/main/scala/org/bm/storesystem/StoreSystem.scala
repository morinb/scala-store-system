package org.bm.storesystem

import java.io.InputStream
import java.util.Properties

import scala.io.Source

/**
 * .
 * @author Baptiste Morin
 */
trait StoreSystem {
  def store(is: InputStream, filename: String)

  def serve(filename: String)

  def config: Properties
}
