package models

import test._

import scala.concurrent.ExecutionContext.Implicits.global

class UserSpec extends Specification {

  "User.getById" should {

    "return a single user" in new App {

      val created = sync { User.create("some.one@example.com","asdfasdf") }

      val actual = created match {
        case Some(expected:User) => sync {
          User.getById(1)
        }
        case _ => failure("Couldn't create a user")
      }

      actual match {
        case Some(actual:User) => {
          actual.id    mustEqual 1
          actual.email mustEqual "some.one@example.com"
        }
        case _ => failure("Couldn't retrieve the user by the given id.")
      }
    }

    "return nothing if the user does not exist" in new App {

      sync { User.getById(1) } match {
        case Some(user:User) => failure("This user should not exist.")
        case _ => success
      }
    }
  }

  "User.getByEmail" should {

    "return a single user when the email exists" in new App {

      val created = sync { User.create("some.one@example.com","asdfasdf") }

      val actual = created match {
        case Some(created:User) => sync {
          User.getByEmail("some.one@example.com")
        }
        case _ => failure("Couldn't create a user")
      }

      actual match {
        case Some(actual:User) => {
          actual mustEqual created.get
        }
        case _ => failure("Couldn't retrieve the user who matches this email address.")
      }
    }

    "return nothing when no User has that email address" in new App {

      val actual = sync { User.getByEmail("some.one@example.com") }

      if (actual.isDefined)
        failure("This user shouldn't exist.")
    }
  }

  "User.create" should {

    "create a single user when the email does not exist" in new App {

      val created = sync { User.create("some.one@example.com","asdfasdf") }

      created match {
        case Some(actual:User) => {
          actual.id    mustEqual 1 
          actual.email mustEqual "some.one@example.com"
        }
        case _ => failure("Couldn't create a user")
      }

      sync { User.getByEmail("some.one@example.com") } match {
        case Some(actual:User) => {
          actual.id    mustEqual 1 
          actual.email mustEqual "some.one@example.com"
        }
        case _ => failure("Didn't persist user")
      }
    }

    "do nothing if the given email already exists" in new App {

      val existing = sync { User.create("already.exists@example.com","asdfasdf") }

      sync { User.getByEmail("already.exists@example.com") } match {
        case Some(actual:User) => {
          actual.id    mustEqual 1 
          actual.email mustEqual "already.exists@example.com"
        }
        case _ => failure("Didn't persist user")
      }

      val attempted = sync { User.create("already.exists@example.com","asdfasdf") }

      if (attempted.isDefined)
        failure("Should not have created a new User -- one already exists with this email address.")

    }
  }
}