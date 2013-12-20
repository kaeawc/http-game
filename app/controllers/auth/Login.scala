package controllers.auth

import models.User

import play.api.mvc._
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

object Login extends Controller {

  val form = Form(
    tuple(
      "email" -> email,
      "password" -> text
  ))

  def post = Action.async {

    implicit request =>

    form.bindFromRequest match {
      case form:Form[(String,String)] if !form.hasErrors => {

        val (email,password) = form.get

        User.authenticate(email, password) map {
          case Some(user:User) => Accepted(user.toPublic)
          case _ => Unauthorized(Json.obj("reason" -> "Could not authenticate User."))
        }
      }
      case _ =>
        Future { BadRequest(Json.obj("reason" -> "Invalid request to Login")) }
    }
  }
}