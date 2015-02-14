package org.bm.storesystem

import java.util.Properties

import scala.io.Source

/**
 * .
 * @author Baptiste Morin
 */
object StoreSystemConfigReader {
  val STORAGE_CONFIG_FILENAME = "storage-config.properties"
  protected[storesystem] var properties: Option[Properties] = None

  def config: Properties = {
    properties match {
      case Some(_) => properties.get

      case None =>
        val props = new Properties()
        props.load(Source fromURL getClass.getResource(s"/$STORAGE_CONFIG_FILENAME") bufferedReader())
        properties = Some(props)
        props

    }
  }
}
