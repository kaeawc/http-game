package controllers.auth

import play.api.mvc._
import play.api.libs.json._

object Logout extends Controller {

  def post = Action {
    Ok(Json.obj("message" -> "You are now logged out."))
    .discardingCookies(DiscardingCookie("auth"))
  }
}