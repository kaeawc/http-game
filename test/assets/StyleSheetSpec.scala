package controllers.auth

import test._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class StyleSheetSpec extends Specification {

  "Invalid StyleSheet paths" should {

    "always respond with NotFound" in new App {

      val response =
        route(FakeRequest(GET, "/css")).get

      status(response) mustEqual 404
    }
  }

  "/css/main.css" should {

    "load main style sheet" in new App {

      val request = FakeRequest(GET, "/css/main.css")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("text/css")
    }
  }

  "/css/bootstrap.min.css" should {

    "load bootstrap style sheet" in new App {

      val request = FakeRequest(GET, "/css/bootstrap.min.css")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("text/css")
    }
  }
}