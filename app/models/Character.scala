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
  created  : Date
)

object Character {

  val chracters =
    long("id") ~
    str("name") ~
    long("user") ~
    date("created") map {
      case        id~name~user~created =>
        Character(id,name,user,created)
    }

}