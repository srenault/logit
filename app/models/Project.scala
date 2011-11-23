package models

import play.Logger
  
import com.mongodb.casbah.Imports._
import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

import db.MongoDB

case class Project(name: String) extends MongoDB {
  def logs: List[Log] = {
    val query = MongoDBObject("project" -> name)
    selectBy(Log.TABLE_NAME, query).map(log => Log(name,log.toString)).toList
  }

  def addLog(log: String) = {
    Logger.debug("Creating log entry with " + log)
    Log.create(Log(name, log))
  }
}

object Project extends MongoDB {
  val TABLE_NAME = "projects"

  def create(project: Project) = {
    val mongoProject = MongoDBObject.newBuilder
    mongoProject += "name" -> project.name

    insert(TABLE_NAME, mongoProject.result)
  }
  
  def findByName(name: String): Option[Project] = {
    val query = MongoDBObject("name" -> name)
    selectOne(TABLE_NAME, query).flatMap { result =>
      Some(Project(result.getAs[String]("name").get))
    }.orElse(None)
  }

  def list(): List[Project] = {
    try {
      selectAll(TABLE_NAME).map { result =>
        Project(result.getAs[String]("name").get)
      }.toList
    } catch {
      case e: Exception => Nil
    }
  }

  implicit object ProjectFormat extends Format[Project] {
    def reads(json: JsValue): Project = json match {
	  case JsObject(m) => Project(fromjson[String](m(JsString("name"))))
	  case _ => throw new RuntimeException("JsObject expected")
    }

    def writes(project: Project): JsValue = {
      JsObject(List((tojson("name").asInstanceOf[JsString], tojson(project.name))))
    }
  }
}
