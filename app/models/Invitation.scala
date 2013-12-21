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
    date("created") ~
    date("expires") ~
    get[Option[Date]]("used") map {
      case        id~code~userFrom~created~expires~used => {

        val usedOn = used match {
          case Some(used:Date) => Some(new DateTime(used))
          case _ => None
        }

        Invitation(id,code,userFrom,new DateTime(created),new DateTime(expires),usedOn)
      }
    }

  def getById(id:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            i.id,
            i.code,
            i.userFrom,
            i.created,
            i.expires,
            i.used
          FROM invitation i
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
            i.id,
            i.code,
            i.userFrom,
            i.created,
            i.expires,
            i.used
          FROM invitation i
          WHERE userFrom = {userFrom};
        """
      ).on(
        'userFrom -> userFrom
      ).as(invitations *)
    }
  }

  def getByCode(code:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            i.id,
            i.code,
            i.userFrom,
            i.created,
            i.expires,
            i.used
          FROM invitation i
          WHERE code = {code};
        """
      ).on(
        'code -> code
      ).as(invitations.singleOpt)
    }
  }

  def countAll = Future {
    DB.withConnection { implicit connection =>
      val result = SQL(
        """
          SELECT COUNT(1) count
          FROM invitation i;
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
    val expires = created.plusWeeks(1)
    val code = crypto.Salt.string()

    Future {
      DB.withConnection { implicit connection =>
        SQL(
          """
            INSERT INTO invitation (
              code,
              userFrom,
              created,
              expires
            ) VALUES (
              {code},
              {userFrom},
              {created},
              {expires}
            );
          """
        ).on(
          'code     -> code,
          'userFrom -> userFrom,
          'created  -> created.toDate,
          'expires  -> expires.toDate
        ).executeInsert()
      }
    } map {
      case Some(id:Long) =>
        Some(Invitation(
          id,
          code,
          userFrom,
          created,
          expires
        ))
      case _ => {
        Logger.warn("Invitation wasn't created")
        None
      }
    }
  }

}