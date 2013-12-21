package controllers.auth

import test._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class StyleSheetSpec extends Specification {

  "/css/main.css" should {

    "load main style sheet" in new App {

      val request = FakeRequest(GET, "/css/main.css")

      val response = route(request).get

      status(response) mustEqual OK
    }
  }

  "/css/bootstrap.css" should {

    "load bootstrap style sheet" in new App {

      val request = FakeRequest(GET, "/css/bootstrap.css")

      val response = route(request).get

      status(response) mustEqual OK
    }
  }
}