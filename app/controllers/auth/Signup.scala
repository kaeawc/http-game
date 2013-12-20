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
      case form:Form[(String,String,String,String)] if !form.hasErrors => {

        val (email,password,retyped,invitation) = form.get

        User.create(form.email, form.password) map {
          case Some(user:User) => Created("")
          case _ => Unauthorized("")
        }
      }
      case _ =>
        BadRequest("")
    }
  }
}
