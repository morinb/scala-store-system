package org.bm.storesystem

import java.math.BigInteger
import java.security.MessageDigest

/**
 * .
 * @author Baptiste Morin
 */
trait HexHashUtils {

  implicit val DEFAULT_ALGORITHM: String = "MD5"

  def digest(filename: String, nbDigitsDirectory: Int)(implicit algorithm: String): String = {
    val md = MessageDigest.getInstance(algorithm)

    val digest = md.digest(filename.getBytes)
    val hex = toHex(digest)
    if (hex.length > nbDigitsDirectory) {
      hex.substring(0, nbDigitsDirectory)
    } else {
      hex
    }
  }


  def toHex(bytes: Array[Byte]): String = {
    val bi = new BigInteger(1, bytes)
    String.format("%0" + (bytes.length << 1) + "x", bi)
  }
}
