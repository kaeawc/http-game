package controllers

import play.api.mvc._

object Logout extends Controller {

  def post = Action { Unauthorized("") }

}