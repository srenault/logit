package models

import play.Logger

import com.mongodb.casbah.Imports._
import scala.util.parsing.json._

import sjson.json._
import DefaultProtocol._
import JsonSerialization._

import db.MongoDB

case class Log(projectName: String, data: String = "{}") extends MongoDB {

  lazy val dataMap: Map[String, Any] = JSON.parseFull(data) match {
    case Some(data: Map[String, Any]) => data
    case _ => Map[String, Any]()
  }
}

object Log extends MongoDB {
  
  val TABLE_NAME = "logs"
  
  def create(log: Log) = {
    val mongoLog = MongoDBObject.newBuilder
    
    val logData = JSON.parseFull(log.data) match {
      case Some(data: Map[String, Any]) => {
        mongoLog ++= data 
        mongoLog +=  "project" -> log.projectName
        insert(TABLE_NAME, mongoLog.result)
      }
      case _ => Logger.warn("Failed to create an log entry")
    }
  }

  implicit val LogFormat: Format[Log] = asProduct2("projectName", "data")(Log.apply)(Log.unapply(_).get)
}
