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

case class User(
  id       : Long,
  email    : String,
  password : String,
  salt     : String,
  created  : Date
)

object User {

}