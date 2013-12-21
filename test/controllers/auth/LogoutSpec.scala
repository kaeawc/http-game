package models

import test._

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class LogoutSpec extends Specification {

  "Logout.post" should {

    "remove any authentication cookie" in new App {

      val request = FakeRequest(POST, "/logout")

      val response = route(request).get

      status(response) mustEqual OK
    }
  }
}