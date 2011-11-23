package models

import play.Logger

import com.mongodb.casbah.Imports._
import scala.util.parsing.json._

import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

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
    
    JSON.parseFull(log.data) match {
      case Some(data: Map[String, Any]) => {
        mongoLog ++= data 
        mongoLog +=  "project" -> log.projectName
        insert(TABLE_NAME, mongoLog.result)
      }
      case _ => Logger.warn("Failed to create an log entry")
    }
  }
  
  implicit object LogFormat extends Format[Log] {
    def reads(json: JsValue): Log = json match {
	  case JsObject(m) => Log(fromjson[String](m(JsString("projectName"))),
							  fromjson[String](m(JsString("data"))))
	  case _ => throw new RuntimeException("JsObject expected")
    }

    def writes(log: Log): JsValue = {
      JsObject(List(
        (tojson("projectName").asInstanceOf[JsString], tojson(log.projectName)),
        (tojson("data").asInstanceOf[JsString], JsValue.fromString(log.data))
      ))
    }
  }
}
