package controllers.auth

import models.User

import play.api.mvc._
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

object Signup extends Controller {

  type SignupForm = forms.Signup

  val form = Form(
    tuple(
      "email" -> email,
      "password" -> text,
      "retyped" -> text,
      "invitation" -> text
    )(forms.Signup.apply)(forms.Signup.unapply)
  )

  def post = Action.async {

    implicit request =>

    form.bindFromRequest match {
      case form:SignupForm if !form.hasErrors => {

        val signup = form.get

        User.create(signup.email, signup.password) map {
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

            Created(user.toPublic).withCookies(cookie)
          }
          case _ => InternalServerError(Json.obj("reason" -> "Could not create a User."))
        }
      }
      case _ =>
        Future { BadRequest(Json.obj("reason" -> "Invalid request to Login")) }
    }
  }
}
