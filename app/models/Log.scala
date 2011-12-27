package models

import play.Logger

import com.mongodb.casbah.Imports._
import scala.util.parsing.json._

import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

import db.MongoDB

object LogTime {
  import java.util.Date
  def now: Long = new Date().getTime()
}

/**
 * Representing a simple log.
 */
case class Log(projectName: String, dataJSON: String = "{}", receivedAt: Long = LogTime.now) {

  /**
   * Log's data.
   * @return Mapping key/value matching to a log pattern.
   */
  lazy val data: Map[String, Any] = JSON.parseFull(dataJSON) match {
    case Some(d: Map[String, Any]) => d
    case _ => Map[String, Any]()
  }
}

/**************************/
/* A simple Log           */
/*************************/
object Log extends MongoDB("logs") {

  def apply(projectName: String, dataJSON: JsValue, receivedAt: Long = LogTime.now): Log = {
    Log(projectName, dataJSON.toString, receivedAt)
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
        mongoLog +=  "receivedAt" -> log.receivedAt
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
  def byProject(name: String): List[Log] = {
    val query  = MongoDBObject("project" -> name)
    val results = selectBy(query)
    (for {
      r <- results
      data <- r.getAs[String]("data")
      receivedAt <- r.getAs[Long]("receivedAt")
    } yield(Log(name, data, receivedAt))).toList
  }

  implicit object LogFormat extends Format[Log] {
    def reads(json: JsValue): Log = json match {
      case JsObject(m) => Log(fromjson[String](m(JsString("project"))),
                              fromjson[String](m(JsString("data"))),
                              fromjson[Long](m(JsString("receivedAt"))))
      case _ => throw new RuntimeException("JsObject expected")
    }

    def writes(log: Log): JsValue = {
      JsObject(List(
        (tojson("project").asInstanceOf[JsString], tojson(log.projectName)),
        (tojson("data").asInstanceOf[JsString], JsValue.fromString(log.dataJSON)),
        (tojson("receivedAt").asInstanceOf[JsString], JsValue.fromString(log.receivedAt))
      ))
    }
  }
}

/**************************/
/* Followed Log by User.  */
/*************************/
case class FollowedLog(projectName: String, 
                       dataJSON: String = "{}", 
                       read: Boolean, 
                       marked: Boolean,
                       receivedAt: Long = LogTime.now) extends MongoDB("logs_fw")

object FollowedLog extends MongoDB("logs_fw") {

  def apply(projectName: String, dataJSON: JsValue, read: Boolean, marked: Boolean, receivedAt: Long = LogTime.now): FollowedLog = {
    FollowedLog(projectName, dataJSON.toString, read, marked, receivedAt)
  }

  /**
   * Create a new FollowedLog.
   * @param A new log.
   * @return Either the FollowedLog successfully created, or None if the log have an invalid format.
   */
  def create(log: FollowedLog): Option[FollowedLog] = {
    val mongoFollowedLog = MongoDBObject.newBuilder
    
    JSON.parseFull(log.dataJSON) match {
      case Some(d: Map[String, Any]) => {
        mongoFollowedLog ++= d 
        mongoFollowedLog +=  "project"    -> log.projectName
        mongoFollowedLog +=  "read"       -> log.read
        mongoFollowedLog +=  "marked"     -> log.marked
        mongoFollowedLog +=  "receivedAt" -> log.receivedAt
        insert(mongoFollowedLog.result)
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
  def byProject(name: String): List[FollowedLog] = {
    val query = MongoDBObject("project" -> name)
    val results = selectBy(query)
    (for {
      r          <- results
      data       <- r.getAs[String]("data")
      read       <- r.getAs[Boolean]("read")
      marked     <- r.getAs[Boolean]("marked")
      receivedAt <- r.getAs[Long]("receivedAt")
    } yield(FollowedLog(name, data, read, marked, receivedAt))).toList
  }

  implicit object FollowedLogFormat extends Format[FollowedLog] {
    def reads(json: JsValue): FollowedLog = json match {
      case JsObject(m) => FollowedLog(fromjson[String](m(JsString("project"))),
                                      fromjson[String](m(JsString("data"))),
                                      fromjson[Boolean](m(JsString("read"))),
                                      fromjson[Boolean](m(JsString("marked"))),
                                      fromjson[Long](m(JsString("receivedAt")))
                                     )
      case _ => throw new RuntimeException("JsObject expected")
    }

    def writes(log: FollowedLog): JsValue = {
      JsObject(List(
        (tojson("project").asInstanceOf[JsString], tojson(log.projectName)),
        (tojson("data").asInstanceOf[JsString], JsValue.fromString(log.dataJSON)),
        (tojson("read").asInstanceOf[JsString], tojson(log.read)),
        (tojson("marked").asInstanceOf[JsString], tojson(log.marked)),
        (tojson("receivedAt").asInstanceOf[JsString], tojson(log.receiveAt))
      ))
    }
  }
}

/**************************/
/* Debugged Log by User. */
/*************************/
case class DebuggedLog(projectName: String, 
                       sessionName: String, 
                       dataJSON: String = "{}", 
                       read: Boolean, 
                       marked: Boolean,
                       receivedAt: Long = LogTime.now) extends MongoDB("logs_db")

object DebuggedLog extends MongoDB("logs_db") {

  def apply(projectName: String, sessionName: String, dataJSON: JsValue, read: Boolean, marked: Boolean, receivedAt: Long = LogTime.now): DebuggedLog = {
    DebuggedLog(projectName, sessionName, dataJSON.toString, read, marked, receivedAt)
  }

  /**
   * Create a new DebuggedLog.
   * @param A new log.
   * @return Either the DebuggedLog successfully created, or None if the log have an invalid format.
   */
  def create(log: DebuggedLog): Option[DebuggedLog] = {
    val mongoDebuggedLog = MongoDBObject.newBuilder
    
    JSON.parseFull(log.dataJSON) match {
      case Some(d: Map[String, Any]) => {
        mongoDebuggedLog ++= d 
        mongoDebuggedLog +=  "project" -> log.projectName
        mongoDebuggedLog +=  "session" -> log.sessionName
        mongoDebuggedLog +=  "read"    -> log.read
        mongoDebuggedLog +=  "marked"  -> log.marked
        mongoDebuggedLog +=  "receivedAt"  -> log.receivedAt
        insert(mongoDebuggedLog.result)
        Some(log)
      }
      case _ => Logger.warn("Failed to create an log entry"); None
    }
  }

  /**
   * Find logs by Session name.
   * @param Project Name 
   * @param Session name
   * @return The found logs.
   */
  def bySession(projectName: String, sessionName: String): List[DebuggedLog] = {
    val query = MongoDBObject("project" -> projectName,
                              "session" -> sessionName)
    val results = selectBy(query)
    (for {
      r          <- results
      data       <- r.getAs[String]("data")
      read       <- r.getAs[Boolean]("read")
      marked     <- r.getAs[Boolean]("marked")
      receivedAt <- r.getAs[Long]("receivedAt")
    } yield(DebuggedLog(projectName, sessionName, data, read, marked))).toList
  }

  implicit object DebuggedLogFormat extends Format[DebuggedLog] {
    def reads(json: JsValue): DebuggedLog = json match {
      case JsObject(m) => DebuggedLog(fromjson[String](m(JsString("project"))),
                                      fromjson[String](m(JsString("session"))),
                                      fromjson[String](m(JsString("data"))),
                                      fromjson[Boolean](m(JsString("read"))),
                                      fromjson[Boolean](m(JsString("marked"))),
                                      fromjson[Long](m(JsString("receivedAt")))
                                     )
      case _ => throw new RuntimeException("JsObject expected")
    }

    def writes(log: DebuggedLog): JsValue = {
      JsObject(List(
        (tojson("project").asInstanceOf[JsString], tojson(log.projectName)),
        (tojson("session").asInstanceOf[JsString], tojson(log.sessionName)),
        (tojson("data").asInstanceOf[JsString], JsValue.fromString(log.dataJSON)),
        (tojson("read").asInstanceOf[JsString], tojson(log.read)),
        (tojson("marked").asInstanceOf[JsString], tojson(log.marked)),
        (tojson("receivedAt").asInstanceOf[JsString], tojson(log.receivedAt))
      ))
    }
  }
}

case class DebbuggedSession(projectName: String, startTime: Long = LogTime.now, started: Boolean) {
  def logs: List[DebuggedLog] = Nil
}
