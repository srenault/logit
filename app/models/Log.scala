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
case class Log(projectName: String, dataJSON: String = "{}", read: Boolean = false, debug: Boolean: false) {

  /**
   * Log's data.
   * @return Mapping key/value matching to a log pattern.
   */
  lazy val data: Map[String, Any] = JSON.parseFull(dataJSON) match {
    case Some(d: Map[String, Any]) => d
    case _ => Map[String, Any]()
  }
}

object Log extends MongoDB {
  
  val TABLE_NAME = "logs"

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
        insert(TABLE_NAME, mongoLog.result)
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
    selectBy(Log.TABLE_NAME, query).map(log => Log(name,log.toString)).toList
  }

  implicit val LogFormat: Format[Log] = asProduct2("projectName", "dataJSON")(Log.apply)(Log.unapply(_).get)
}
