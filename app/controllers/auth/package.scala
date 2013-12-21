package controllers

import play.api.libs.Crypto

package object auth {

  def encrypt(value:Long):String =
    encrypt(value.toString)

  def encrypt(value:String):String =
    Crypto.encryptAES(value)

  def decrypt(value:String):String =
    Crypto.decryptAES(value)
}