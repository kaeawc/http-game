package controllers

import models.User
import play.api.mvc.Cookie
import play.api.libs.Crypto

package object auth {

  def encrypt(value:Long):String =
    encrypt(value.toString)

  def encrypt(value:String):String =
    Crypto.encryptAES(value)

  def decrypt(value:String):String =
    Crypto.decryptAES(value)

  def authorizedCookie(user:User) = new Cookie(
    name        = "auth",
    value       = encrypt(user.id),
    maxAge      = Some(31536000),
    path        = "/",
    domain      = None,
    secure      = false,
    httpOnly    = true
  )
}