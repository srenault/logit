package models

import com.mongodb.casbah.Imports._
import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

import play.Logger

import db.MongoDB

case class User(pseudo: String) extends MongoDB {
}

object User {
}
