package controllers.auth

import test._
import models._

import play.api.mvc._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class SignupSpec extends Specification {

  "Signup.post" should {

    "respond with BadRequest when no data is provided" in new App {

      val request = FakeRequest(POST, "/signup")

      val response = route(request).get

      val expected = Json.obj("reason" -> "Invalid request to Signup")

      val actual = Json.parse(contentAsString(response))

      status(response) mustEqual 400

      actual mustEqual expected
    }

    "respond with BadRequest when only partial data is provided" in new App {

      val request = FakeRequest(POST, "/signup")

      val data = Json.obj(
        "email" -> "some.one@example.com"
      )

      val response = route(request,data).get

      val expected = Json.obj("reason" -> "Invalid request to Signup")

      val actual = Json.parse(contentAsString(response))

      status(response) mustEqual 400

      actual mustEqual expected
    }

    "respond with Unauthorized when email and password don't match an existing User" in new App {

      val request = FakeRequest(POST, "/signup")

      val data = Json.obj(
        "email"      -> "some.one@example.com",
        "password"   -> "asdfasdf",
        "retyped"    -> "fdsafdsa",
        "invitation" -> "asdfasdf"
      )

      val response = route(request,data).get

      val expected = Json.obj("reason" -> "Could not authenticate User.")

      val actual = Json.parse(contentAsString(response))

      status(response) mustEqual 401

      actual mustEqual expected
    }

    "respond with Accepted when email and password match an existing User" in new App {

      val request = FakeRequest(POST, "/signup")

      val data = Json.obj(
        "email"      -> "some.one@example.com",
        "password"   -> "asdfasdf",
        "retyped"    -> "asdfasdf",
        "invitation" -> "asdfasdf"
      )

      val response = route(request,data).get

      val actual = Json.parse(contentAsString(response))

      status(response) mustEqual 201

      actual \ "id" mustEqual JsNumber(1)
      actual \ "email" mustEqual JsString("some.one@example.com")

      val user = sync { User.getByEmail("some.one@example.com") }

      user match {
        case Some(User(1,"some.one@example.com",password,salt,created)) =>
          success
        case _ =>
          failure("Did not persist User in Database")
      }
    }
  }
}