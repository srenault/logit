package models

import play.Logger

import com.mongodb.casbah.Imports._
import scala.util.parsing.json._

import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

import db.MongoDB

/**
 * Representing a simple log.
 */
case class Log(projectName: String, dataJSON: String = "{}") {

  /**
   * Log's data.
   * @return Mapping key/value matching to a log pattern.
   */

  lazy val data: Map[String, Any] = JSON.parseFull(dataJSON) match {
    case Some(d: Map[String, Any]) => d
    case _ => Map[String, Any]()
  }
}

object Log extends MongoDB("logs") {

  def apply(projectName: String, dataJSON: JsValue): Log = {
    Log(projectName, dataJSON.toString)
  }

  /**
   * Create a new Log.
   * @param A new log.
   * @return Either the Log successfully created, or None if the log have an invalid format.
   */
  def create(log: Log): Option[Log] = {
    val mongoLog = MongoDBObject.newBuilder
    
    JSON.parseFull(log.dataJSON) match {
      case Some(d: Map[String, Any]) => {
        mongoLog ++= d 
        mongoLog +=  "project" -> log.projectName
        insert(mongoLog.result)
        Some(log)
      }
      case _ => Logger.warn("Failed to create an log entry"); None
    }
  }

  /**
   * Find logs by Project name.
   * @param Project name
   * @return The found Projects.
   */
  def findByProject(name: String): List[Log] = {
    val query = MongoDBObject("project" -> name)
    selectBy(query).map(log => Log(name,log.toString)).toList
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
        (tojson("data").asInstanceOf[JsString], JsValue.fromString(log.dataJSON))
      ))
    }
  }
}
