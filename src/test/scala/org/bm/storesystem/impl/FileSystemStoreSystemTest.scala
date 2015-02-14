package org.bm.storesystem.impl

import java.io.{StringReader, InputStream}

import org.bm.storesystem.StoreSystem
import org.scalatest.FunSuite

import scala.io.Source

/**
 * .
 * @author Baptiste Morin
 */
class FileSystemStoreSystemTest extends FunSuite {
  val filename = "ToBeIndexed.txt"

  test("Store") {

    val system: StoreSystem = StoreSystem()

    val inputStream = getClass.getResourceAsStream(s"/$filename")

    // Storing
    val maybePath = system.store(inputStream, filename)

    assert(maybePath !== None)

    val path = maybePath.get

    println(s"stored file $filename in $path")

    system.close()

  }

  test("Serve") {

    val system: StoreSystem = StoreSystem()

    val stream: InputStream = system.serve(filename)

    assert(stream !== null)

    Source.fromInputStream(stream).getLines().foreach(println)


    system.close()

  }

}
