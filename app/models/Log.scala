package models

import play.Logger
import play.api.libs.json._

import com.mongodb.casbah.Imports._

import db.MongoDB

object LogTime {
  import java.util.Date
  def now: Long = new Date().getTime()
}

/**
 * Representing a simple log.
 */
case class Log(projectName: String, dataJSON: JsObject, receivedAt: Long = LogTime.now) {

  lazy val data: Map[String, JsValue] = dataJSON.as[Map[String, JsValue]]
}

object Log extends MongoDB("logs") {
  /**
   * Create a new Log.
   * @param A new log.
   * @return Either the Log successfully created, or None if the log have an invalid format.
   */
  def create(log: Log): Option[Log] = {
    val mongoLog = MongoDBObject.newBuilder
    
    mongoLog ++= log.dataJSON.value
    mongoLog +=  "project" -> log.projectName
    mongoLog +=  "receivedAt" -> log.receivedAt
    insert(mongoLog.result)
    Some(log)
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
      dataJSON <- r.getAs[JsObject]("dataJSON")
      receivedAt <- r.getAs[Long]("receivedAt")
    } yield(Log(name, dataJSON, receivedAt))).toList
  }

  implicit object LogFormat extends Format[Log] {
    def reads(json: JsValue): Log = Log(
      (json \ "project").as[String],
      (json \ "dataJSON") match {
        case d: JsObject => d
        case _ => JsObject(Map[String, JsValue]())
       },
      (json \ "receivedAt").as[Long])

    def writes(log: Log): JsValue = JsObject(Map(
      "project" -> JsString(log.projectName),
      "dataJSON" -> log.dataJSON,
      "receivedAt" -> JsNumber(log.receivedAt)
    ))
  }
}

case class FollowedLog(projectName: String, 
                       data: JsObject, 
                       read: Boolean, 
                       marked: Boolean,
                       receivedAt: Long = LogTime.now) extends MongoDB("logs_fw")

object FollowedLog extends MongoDB("logs_fw") {
  /**
   * Create a new FollowedLog.
   * @param A new log.
   * @return Either the FollowedLog successfully created, or None if the log have an invalid format.
   */
  def create(log: FollowedLog): Option[FollowedLog] = {
    val mongoFollowedLog = MongoDBObject.newBuilder
    mongoFollowedLog ++= log.data.value
    mongoFollowedLog +=  "project"    -> log.projectName
    mongoFollowedLog +=  "read"       -> log.read
    mongoFollowedLog +=  "marked"     -> log.marked
    mongoFollowedLog +=  "receivedAt" -> log.receivedAt
    insert(mongoFollowedLog.result)
    Some(log)
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
      data       <- r.getAs[JsObject]("data")
      read       <- r.getAs[Boolean]("read")
      marked     <- r.getAs[Boolean]("marked")
      receivedAt <- r.getAs[Long]("receivedAt")
    } yield(FollowedLog(name, data, read, marked, receivedAt))).toList
  }

  implicit object FollowedLogFormat extends Format[FollowedLog] {
    def reads(json: JsValue): FollowedLog = FollowedLog(
      (json \ "project").as[String],
      (json \ "data") match {
        case d: JsObject => d
        case _ => JsObject(Map[String, JsValue]())
       },
      (json \ "read").as[Boolean],
      (json \ "marked").as[Boolean],
      (json \ "receivedAt").as[Long])

    def writes(log: FollowedLog): JsValue = JsObject(Map(
      "project" -> JsString(log.projectName),
      "data" -> log.data,
      "read" -> JsBoolean(log.read),
      "marked" -> JsBoolean(log.marked),
      "receivedAt" -> JsNumber(log.receivedAt)
    ))
  }
}

case class DebuggedLog(projectName: String, 
                       sessionName: String, 
                       data: JsObject, 
                       read: Boolean, 
                       marked: Boolean,
                       receivedAt: Long = LogTime.now) extends MongoDB("logs_db")

object DebuggedLog extends MongoDB("logs_db") {
  /**
   * Create a new DebuggedLog.
   * @param A new log.
   * @return Either the DebuggedLog successfully created, or None if the log have an invalid format.
   */
  def create(log: DebuggedLog): Option[DebuggedLog] = {
    val mongoDebuggedLog = MongoDBObject.newBuilder
    mongoDebuggedLog ++= log.data.value
    mongoDebuggedLog +=  "project" -> log.projectName
    mongoDebuggedLog +=  "session" -> log.sessionName
    mongoDebuggedLog +=  "read"    -> log.read
    mongoDebuggedLog +=  "marked"  -> log.marked
    mongoDebuggedLog +=  "receivedAt"  -> log.receivedAt
    insert(mongoDebuggedLog.result)
    Some(log)
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
      data       <- r.getAs[JsObject]("data")
      read       <- r.getAs[Boolean]("read")
      marked     <- r.getAs[Boolean]("marked")
      receivedAt <- r.getAs[Long]("receivedAt")
    } yield(DebuggedLog(projectName, sessionName, data, read, marked))).toList
  }

  implicit object DebuggedLogFormat extends Format[DebuggedLog] {
    def reads(json: JsValue): DebuggedLog = DebuggedLog(
      (json \ "project").as[String],
      (json \ "session").as[String],
      (json \ "data") match {
        case d: JsObject => d
        case _ => JsObject(Map[String, JsValue]())
       },
      (json \ "read").as[Boolean],
      (json \ "marked").as[Boolean],
      (json \ "receivedAt").as[Long])

    def writes(log: DebuggedLog): JsValue = JsObject(Map(
      "project"    -> JsString(log.projectName),
      "session"    -> JsString(log.sessionName),
      "data"       -> log.data,
      "read"       -> JsBoolean(log.read),
      "marked"     -> JsBoolean(log.marked),
      "receivedAt" -> JsNumber(log.receivedAt)
    ))
  }
}

case class DebbuggedSession(projectName: String, startTime: Long = LogTime.now, started: Boolean) {
  def logs: List[DebuggedLog] = Nil
}
