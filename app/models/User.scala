package models

import anorm._
import anorm.SqlParser._

import play.api.Logger
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._

import scala.concurrent.{Future,ExecutionContext}

import ExecutionContext.Implicits.global

case class User(
  id       : Long,
  email    : String,
  password : String,
  salt     : String,
  created  : DateTime = now
) {

  def toPublic = Json.obj(
    "id"      -> id,
    "email"   -> email,
    "created" -> created
  )

}

object User {

  import crypto.Hexadecimal._

  val users =
    long("id") ~
    str("email") ~
    str("password") ~
    str("salt") ~
    date("created") map {
      case   id~email~password~salt~created =>
        User(id,email,password,salt,new DateTime(created))
    }

  def getById(id:Long) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            u.id,
            u.email,
            u.password,
            u.salt,
            u.created
          FROM user u
          WHERE id = {id};
        """
      ).on(
        'id -> id
      ).as(users.singleOpt)
    }
  }

  def getByEmail(email:String) = Future {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT
            u.id,
            u.email,
            u.password,
            u.salt,
            u.created
          FROM user u
          WHERE email = {email};
        """
      ).on(
        'email -> email
      ).as(users.singleOpt)
    }
  }

  def countAll = Future {
    DB.withConnection { implicit connection =>
      val result = SQL(
        """
          SELECT COUNT(1) count
          FROM user u;
        """
      ).apply()

      try {
        Some(result.head[Long]("count"))
      } catch {
        case e:Exception => None
      }
    }
  }

  def create(email:String,password:String):Future[Option[User]] = {

    val salt              = crypto.PBKDF2.createSalt()
    val hashedPassword    = crypto.PBKDF2.hash(password,salt)
    val storedSalt:String = salt
    val created           = now

    getByEmail(email) map {
      case Some(user:User) => {
        Logger.warn("User already created")
        None
      }
      case _ => {
        DB.withConnection { implicit connection =>
          SQL(
            """
              INSERT INTO user (
                email,
                password,
                salt,
                created
              ) VALUES (
                {email},
                {password},
                {salt},
                {created}
              );
            """
          ).on(
            'email    -> email,
            'password -> hashedPassword,
            'salt     -> storedSalt,
            'created  -> created.toDate
          ).executeInsert()
        }
      }
    } map {
      case Some(id:Long) => {
        Some(User(
          id,
          email,
          hashedPassword,
          storedSalt,
          created
        ))
      }
      case _ => {
        Logger.warn("User wasn't created")
        None
      }
    }
  }

  def authenticate(email:String,password:String) =
    getByEmail(email) map {
      case Some(user:User) => {

        val hashedPassword:String = crypto.PBKDF2.hash(password,user.salt)

        if(password == user.password)
          Some(user)
        else
          None
      }
      case _ => None
    }
}
