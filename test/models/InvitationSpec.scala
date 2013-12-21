package models

import test._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class InvitationSpec extends Specification {

  "Invitation.getById" should {

    "return a single invitation" in new App {

      sync(Invitation.create(1)) match {
        case Some(expected:Invitation) => {
          Invitation.getById(1) map {
            case Some(actual:Invitation) => {
              expected mustEqual actual
            }
            case _ => failure("Couldn't retrieve the invitation who matches this user address.")
          }
        }
        case _ => failure("Couldn't create a invitation")
      }
    }

    "return nothing if the invitation does not exist" in new App {

      Invitation.getById(1) map {
        case Some(invitation:Invitation) => failure("This invitation should not exist.")
        case _ => success
      }
    }
  }

  "Invitation.getByUser" should {

    "return a empty list when a User has no Invitations" in new App {

      val actual = sync { Invitation.getByUser(1) }

      actual mustEqual Nil
    }

    "return a non-empty list when a User has Invitations" in new App {

      val actual = sync(Invitation.create(1)) match {
        case Some(created:Invitation) => sync {
          Invitation.getByUser(1)
        }
        case _ => failure("Couldn't create a invitation")
      }

      actual.head match {
        case actual:Invitation => {
          actual.id       mustEqual 1
          actual.userFrom mustEqual 1
        }
        case _ => failure("Couldn't retrieve the invitations who matches this user address.")
      }
    }
  }

  "Invitation.create" should {

    "create a single Invitation when the User exists" in new App {

      val invitation = sync(User.create("some.one@example.com","asdfasdf")) match {
        case Some(user:User) => sync {
          Invitation.create(1)
        }
        case _ => failure("Couldn't create a User")
      }

      val list = invitation match {
        case Some(invitation:Invitation) => {
          invitation.id       mustEqual 1
          invitation.userFrom mustEqual 1
          invitation.expires  mustEqual invitation.created.plusWeeks(1)

          sync { Invitation.getByUser(1) }
        }
        case _ => failure("Couldn't create a Invitation")
      }

      if (list.isEmpty) failure("Didn't persist Invitation")
    }
  }
}