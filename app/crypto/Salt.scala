package crypto

import java.security._

trait Salt {
  
  val saltByteSize:Int
  
  def createSalt(size:Int = saltByteSize):Array[Byte] = {
    val random = new SecureRandom()
    val salt   = new Array[Byte](size)
    random.nextBytes(salt)
    salt
  }
}

object Salt extends Salt {

  val saltByteSize = 16

  def string(size:Int = saltByteSize) =
    Hexadecimal.bytes2hex(createSalt(size))

}