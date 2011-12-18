package models

import play.Logger
  
import com.mongodb.casbah.Imports._
import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

import db.MongoDB

case class Project(name: String, debug: Boolean = false) extends MongoDB {

  /**
   * List logs' project.
   * @return Logs' project.
   */
  def logs(): List[Log] = Log.findByProject(name)

  /** Add up a new log on project' stack.
   * @return The Log successfully added.
   * @param A new log.
   */
  def addUpLog(log: String): Option[Log] = {
    Logger.debug("Creating log entry with " + log)
    Log.create(Log(name, log))
  }
}

object Project extends MongoDB {

  val TABLE_NAME = "projects"

  /**
   * Create a new Project.
   * @return The Project successfully created.
   */
  def create(project: Project): Project = {
    val mongoProject = MongoDBObject.newBuilder
    mongoProject += "name" -> project.name

    insert(TABLE_NAME, mongoProject.result)
    project
  }

  /**
   * Find a Project by name.
   * @param Project name
   * @return Either the found Project, or None.
   */
  def findByName(name: String): Option[Project] = {
    val query = MongoDBObject("name" -> name)
    selectOne(TABLE_NAME, query).flatMap { result =>
      Some(Project(result.getAs[String]("name").get))
    }.orElse(None)
  }

  /**
   * Retrieve all Projects.
   * @return Projects list.
   */
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
