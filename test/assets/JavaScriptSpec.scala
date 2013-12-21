package controllers.auth

import test._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class JavaScriptSpec extends Specification {

  "/js/jquery-2.0.3.min.js" should {

    "load JQuery" in new App {

      val request = FakeRequest(GET, "/js/jquery-2.0.3.min.js")

      val response = route(request).get

      status(response) mustEqual OK
    }
  }
}