package controllers.auth

import test._

import play.api.mvc._
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

      val expected = Cookie(
        "auth","",Some(-86399),"/",None,false,true
      )

      cookies(response).get("auth") match {
        case Some(actual:Cookie) => {
          actual.name     mustEqual expected.name
          actual.value    mustEqual expected.value
          actual.maxAge   mustEqual expected.maxAge
          actual.path     mustEqual expected.path
          actual.domain   must      beNone
          actual.secure   mustEqual expected.secure
          actual.httpOnly mustEqual expected.httpOnly
        }
        case _ => failure("Should have responded with an expiration cookie.")
      }
    }
  }
}