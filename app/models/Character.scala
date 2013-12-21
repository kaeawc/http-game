package models

import anorm._
import anorm.SqlParser._

import play.api.Logger
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._

import java.util.Date

import scala.concurrent.{Future,ExecutionContext}

import ExecutionContext.Implicits.global

case class Character(
  id       : Long,
  name     : String,
  user     : Long,
  created  : Date = now
)

object Character {

  val characters =
    long("id") ~
    str("name") ~
    long("user") ~
    date("created") map {
      case        id~name~user~created =>
        Character(id,name,user,created)
    }

  def getById(id:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            c.id,
            c.name,
            c.user,
            c.created
          FROM character c
          WHERE id = {id};
        """
      ).on(
        'id -> id
      ).as(characters.singleOpt)
    }
  }

  def getByUser(user:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            c.id,
            c.name,
            c.user,
            c.created
          FROM character c
          WHERE user = {user};
        """
      ).on(
        'user -> user
      ).as(characters *)
    }
  }

  def countAll = Future {
    DB.withConnection { implicit connection =>
      val result = SQL(
        """
          SELECT COUNT(1) count
          FROM character c;
        """
      ).apply()

      try {
        Some(result.head[Long]("count"))
      } catch {
        case e:Exception => None
      }
    }
  }

  def create(name:String,user:Long) = {

    val created = now

    Future {
      DB.withConnection { implicit connection =>
        SQL(
          """
            INSERT INTO character (
              name,
              user,
              created
            ) VALUES (
              {name},
              {user},
              {created}
            );
          """
        ).on(
          'name    -> name,
          'user    -> user,
          'created -> created
        ).executeInsert()
      }
    } map {
      case Some(id:Long) =>
        Some(Character(
          id,
          name,
          user,
          created
        ))
      case _ => {
        Logger.warn("Character wasn't created")
        None
      }
    }
  }

}