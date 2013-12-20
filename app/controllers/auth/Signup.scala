package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Signup extends Controller {

  val form = Form(
    tuple(
      "email" -> email,
      "password" -> text,
      "retyped" -> text,
      "invitation" -> text
  ))

  def post = Action { 

    implicit request =>

    form.bindFromRequest match {
      case form:Form[(String,String,String,String)] if !form.hasErrors =>
        Created("")
      case _ =>
        Unauthorized("")
    }
  }
}
