package controllers.auth

import test._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class ApplicationSpec extends Specification {

  "Application.get" should {

    "load the application" in new App {

      val request = FakeRequest(GET, "/")

      val response = route(request).get

      status(response) mustEqual OK

      contentAsString(response) mustEqual views.html.index().toString()
    }
  }
}