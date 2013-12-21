package controllers.auth

import test._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class ImagesSpec extends Specification {

  "/img/logo.png" should {

    "load logo image" in new App {

      val request = FakeRequest(GET, "/img/logo.png")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("image/png")
    }
  }
}