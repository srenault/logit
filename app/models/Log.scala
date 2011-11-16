package models

import com.mongodb.casbah.Imports._
import scala.util.parsing.json._
import db.MongoDB

case class Log(projectName: String, info: Map[String, Any]) {
}

object Log extends MongoDB {
  val TABLE_NAME = "logs"

  def create(log: Log) = {
    val mongoLog = MongoDBObject.newBuilder
    mongoLog ++= log.info
    mongoLog +=  "project" -> log.projectName
    insert(TABLE_NAME, mongoLog.result)
  }

  def apply(projectName: String, log: String): Log = {
    JSON.parseFull(log) match {
      case Some(json: Map[String, Any]) => Log(projectName, json)
      case _ => Log(projectName, Map[String, Any]())
    }
  }
}
