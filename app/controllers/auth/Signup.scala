package controllers.auth

import models._

import play.api.mvc._
import play.api.data._
import play.api.libs.json._
import play.api.data.Forms._

import scala.concurrent.{Future,ExecutionContext}
import ExecutionContext.Implicits.global

object Signup extends Controller {

  type SignupForm = Form[forms.Signup]

  val form = Form(
    mapping(
      "email" -> email,
      "passwords" -> tuple(
        "password" -> text(minLength = 6),
        "retyped" -> text
      ).verifying(
        "Passwords don't match", passwords => {


          val hasCode = passwords._1 == passwords._2

          if(hasCode)
            println("has good password")
          else
            println("no good password")

          hasCode
        }
      ),
      "invitation" -> text.verifying(
        "Invalid Invitation code", invitation => {

          val hasCode = Invitation.verify(invitation)

          if(hasCode)
            println("has invitation code")
          else
            println("no invitation code")
          

          hasCode
        }
      )
    )(forms.Signup.apply)(forms.Signup.unapply)
  )

  def post = Action.async {

    implicit request =>

    form.bindFromRequest match {
      case form:SignupForm if !form.hasErrors => {

        val signup = form.get

        User.create(signup.email, signup.passwords._1) map {
          case Some(user:User) => Created(user.toPublic).withCookies(authorizedCookie(user))
          case _ => InternalServerError(Json.obj("reason" -> "Could not create a User"))
        }
      }
      case _ =>
        Future { BadRequest(Json.obj("reason" -> "Invalid request to Signup")) }
    }
  }
}
