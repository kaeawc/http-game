package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Signup extends Controller {

  val form = Form(
    tuple(
      "email" -> email,
      "password" -> text,
      "retyped" -> text
  ))

  def post = Action { Unauthorized("") }

}