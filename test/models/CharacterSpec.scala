package models

import test._

import scala.concurrent.{Await,ExecutionContext}
import ExecutionContext.Implicits.global

class CharacterSpec extends Specification {

  "Character.getById" should {

    "return a single character" in new App {

      sync(Character.create("Some Character Name",1)) match {
        case Some(expected:Character) => {
          Character.getById(1) map {
            case Some(actual:Character) => {
              expected mustEqual actual
            }
            case _ => failure("Couldn't retrieve the character who matches this user address.")
          }
        }
        case _ => failure("Couldn't create a character")
      }
    }

    "return nothing if the character does not exist" in new App {

      Character.getById(1) map {
        case Some(character:Character) => failure("This character should not exist.")
        case _ => success
      }
    }
  }

  "Character.getByUser" should {

    "return a empty list when a User has no Characters" in new App {

      val actual = sync { Character.getByUser(1) }

      actual mustEqual Nil
    }

    "return a non-empty list when a User has Characters" in new App {

      val expected = Character(1,"Some Character Name",1)
      val actual = sync(Character.create("Some Character Name",1)) match {
        case Some(created:Character) => sync {
          Character.getByUser(1)
        }
        case _ => failure("Couldn't create a character")
      }

      actual.head match {
        case actual:Character => {
          dateFormat.format(actual.created) mustEqual dateFormat.format(expected.created)
        }
        case _ => failure("Couldn't retrieve the characters who matches this user address.")
      }
    }
  }

  "Character.create" should {

    "create a single Character when the User exists" in new App {

      val character = sync(User.create("some.one@example.com","asdfasdf")) match {
        case Some(user:User) => sync {
          Character.create("Some Character Name",1)
        }
        case _ => failure("Couldn't create a User")
      }

      val list = character match {
        case Some(character:Character) => {
          character.id   mustEqual 1
          character.name mustEqual "Some Character Name"
          character.user mustEqual 1

          sync { Character.getByUser(1) }
        }
        case _ => failure("Couldn't create a Character")
      }

      if (list.isEmpty) failure("Didn't persist Character")
    }
  }
}