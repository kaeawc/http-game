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

      contentType(response) must beSome("application/javascript")
    }
  }

  "/js/jquery-2.0.3.js" should {

    "load minified JQuery" in new App {

      val request = FakeRequest(GET, "/js/jquery-2.0.3.js")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/javascript")
    }
  }

  "/js/jquery-2.0.3.min.map" should {

    "load JQuery's source map" in new App {

      val request = FakeRequest(GET, "/js/jquery-2.0.3.min.map")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/x-navimap")
    }
  }

  "/js/backbone.js" should {

    "load Backbone" in new App {

      val request = FakeRequest(GET, "/js/backbone.js")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/javascript")
    }
  }

  "/js/backbone.min.js" should {

    "load minified Backbone" in new App {

      val request = FakeRequest(GET, "/js/backbone.min.js")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/javascript")
    }
  }

  "/js/modernizer.js" should {

    "load Modernizer" in new App {

      val request = FakeRequest(GET, "/js/modernizer.js")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/javascript")
    }
  }

  "/js/underscore.js" should {

    "load Underscore" in new App {

      val request = FakeRequest(GET, "/js/underscore.js")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/javascript")
    }
  }

  "/js/underscore.min.js" should {

    "load minified Underscore" in new App {

      val request = FakeRequest(GET, "/js/underscore.min.js")

      val response = route(request).get

      status(response) mustEqual OK

      contentType(response) must beSome("application/javascript")
    }
  }
}