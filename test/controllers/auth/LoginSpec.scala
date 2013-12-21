package controllers.auth

import test._
import models._

import play.api.mvc._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class LoginSpec extends Specification {

  "Login.post" should {

    "respond with BadRequest when no data is provided" in new App {

      val request = FakeRequest(POST, "/login")

      val response = route(request).get

      val expected = Json.obj("reason" -> "Invalid request to Login")

      val actual = Json.parse(contentAsString(response))

      status(response) mustEqual 400

      actual mustEqual expected
    }

    "respond with BadRequest when only partial data is provided" in new App {

      val request = FakeRequest(POST, "/login")

      val data = Json.obj(
        "email" -> "some.one@example.com"
      )

      val response = route(request,data).get

      val expected = Json.obj("reason" -> "Invalid request to Login")

      val actual = Json.parse(contentAsString(response))

      status(response) mustEqual 400

      actual mustEqual expected
    }

    "respond with Unauthorized when email and password don't match an existing User" in new App {

      val request = FakeRequest(POST, "/login")

      val data = Json.obj(
        "email" -> "some.one@example.com",
        "password" -> "asdfasdf"
      )

      val response = route(request,data).get

      val expected = Json.obj("reason" -> "Could not authenticate User.")

      val actual = Json.parse(contentAsString(response))

      status(response) mustEqual 401

      actual mustEqual expected
    }

    "respond with Accepted when email and password match an existing User" in new App {

      val created = sync { User.create("some.one@example.com","asdfasdf") }

      if (created.isEmpty) failure("Couldn't create a User")

      val user = created.get

      val request = FakeRequest(POST, "/login")

      val data = Json.obj(
        "email" -> "some.one@example.com",
        "password" -> "asdfasdf"
      )

      val response = route(request,data).get

      val expected = user.toPublic

      val actual = Json.parse(contentAsString(response))

      status(response) mustEqual 202

      actual mustEqual expected
    }
  }
}