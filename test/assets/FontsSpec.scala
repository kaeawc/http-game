package controllers.auth

import test._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class FontsSpec extends Specification {

  "Invalid Font paths" should {

    "always respond with NotFound" in new App {

      val response =
        route(FakeRequest(GET, "/fonts")).get

      status(response) mustEqual 404
    }
  }

  "/fonts/glyphicons-halflings-regular.eot" should {

    "load main style sheet" in new App {

      val request = FakeRequest(GET, "/fonts/glyphicons-halflings-regular.eot")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/vnd.ms-fontobject")
    }
  }

  "/fonts/glyphicons-halflings-regular.svg" should {

    "load main style sheet" in new App {

      val request = FakeRequest(GET, "/fonts/glyphicons-halflings-regular.svg")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("image/svg+xml")
    }
  }

  "/fonts/glyphicons-halflings-regular.ttf" should {

    "load main style sheet" in new App {

      val request = FakeRequest(GET, "/fonts/glyphicons-halflings-regular.ttf")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/x-font-ttf")
    }
  }

  "/fonts/glyphicons-halflings-regular.woff" should {

    "load main style sheet" in new App {

      val request = FakeRequest(GET, "/fonts/glyphicons-halflings-regular.woff")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/font-woff")
    }
  }
}