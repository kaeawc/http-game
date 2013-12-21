package controllers.auth

import models.User

import play.api.mvc._
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

object Login extends Controller {

  type LoginForm = Form[forms.Login]

  val form = Form(
    mapping(
      "email" -> email,
      "password" -> text
    )(forms.Login.apply)(forms.Login.unapply)
  )

  def post = Action.async {

    implicit request =>

    form.bindFromRequest match {
      case form:LoginForm if !form.hasErrors => {

        val login = form.get

        User.authenticate(login.email, login.password) map {
          case Some(user:User) => {

            val cookie = new Cookie(
              name        = "auth",
              value       = encrypt(user.id),
              maxAge      = Some(31536000),
              path        = "/",
              domain      = None,
              secure      = false,
              httpOnly    = true
            )

            Accepted(user.toPublic).withCookies(cookie)
          }
          case _ => Unauthorized(Json.obj("reason" -> "Could not authenticate User."))
        }
      }
      case _ =>
        Future { BadRequest(Json.obj("reason" -> "Invalid request to Login")) }
    }
  }
}