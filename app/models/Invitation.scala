package models

import anorm._
import anorm.SqlParser._

import play.api.Logger
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._

import scala.concurrent.{Future,ExecutionContext}

import ExecutionContext.Implicits.global

case class Invitation(
  id       : Long,
  code     : String,
  userFrom : Long,
  created  : DateTime = now,
  expires  : DateTime = now.plusWeeks(1),
  used     : Option[DateTime] = None
)

object Invitation {

  val invitations =
    long("id") ~
    str("code") ~
    long("userFrom") ~
    date("created") map {
      case        id~code~userFrom~created =>
        Invitation(id,code,userFrom,new DateTime(created))
    }

  def getById(id:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            c.id,
            c.code,
            c.userFrom,
            c.created
          FROM invitation c
          WHERE id = {id};
        """
      ).on(
        'id -> id
      ).as(invitations.singleOpt)
    }
  }

  def getByUser(userFrom:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            c.id,
            c.code,
            c.userFrom,
            c.created
          FROM invitation c
          WHERE userFrom = {userFrom};
        """
      ).on(
        'userFrom -> userFrom
      ).as(invitations *)
    }
  }

  def countAll = Future {
    DB.withConnection { implicit connection =>
      val result = SQL(
        """
          SELECT COUNT(1) count
          FROM invitation c;
        """
      ).apply()

      try {
        Some(result.head[Long]("count"))
      } catch {
        case e:Exception => None
      }
    }
  }

  def create(userFrom:Long) = {

    val created = now
    val code = crypto.Salt.string()

    Future {
      DB.withConnection { implicit connection =>
        SQL(
          """
            INSERT INTO invitation (
              code,
              userFrom,
              created
            ) VALUES (
              {code},
              {userFrom},
              {created}
            );
          """
        ).on(
          'code     -> code,
          'userFrom -> userFrom,
          'created  -> created.toDate
        ).executeInsert()
      }
    } map {
      case Some(id:Long) =>
        Some(Invitation(
          id,
          code,
          userFrom,
          created
        ))
      case _ => {
        Logger.warn("Invitation wasn't created")
        None
      }
    }
  }

}