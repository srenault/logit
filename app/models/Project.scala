package models

import play.Logger
  
import com.mongodb.casbah.Imports._
import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

import db.MongoDB

case class Project(name: String, debug: Boolean = false) extends MongoDB("projects") {

  /**
   * List logs' project.
   * @return Logs' project.
   */
  def logs: List[Log] = Log.byProject(name)

  /** Add up a new log on project' stack.
   * @return The Log successfully added.
   * @param A new log.
   */
  def addUpLog(log: String): Option[Log] = {
    Log.create(Log(name, JsValue.fromString(log)))
  }
}

object Project extends MongoDB("projects") {

  /**
   * Create a new Project.
   * @return The Project successfully created.
   */
  def create(project: Project): Project = {
    val mongoProject = MongoDBObject.newBuilder
    mongoProject += "name" -> project.name
    mongoProject += "debug" -> project.debug

    insert(mongoProject.result)
    project
  }

  /**
   * Find a Project by name.
   * @param Project name
   * @return Either the found Project, or None.
   */
  def byName(name: String): Option[Project] = {
    val query = MongoDBObject("name" -> name)
    selectOne(query).flatMap { result =>
      Some(Project(result.getAs[String]("name").get,
                   result.getAs[Boolean]("debug").get))
    }.orElse(None)
  }

  /**
   * Retrieve all Projects.
   * @return Projects list.
   */
  def list(): List[Project] = {
    selectAll().map { result =>
      Project(result.getAs[String]("name").get,
              result.getAs[Boolean]("debug").get)
    }.toList
  }

  implicit val ProjectFormat: Format[Project] = asProduct2("name", "debug")(Project.apply)(Project.unapply(_).get)
}

case class FollowedProject(name: String, pseudo: String, dirty: Boolean = false) extends MongoDB("projects_fw") {
  def logs = Nil
}
object FollowedProject extends MongoDB("projects_fw"){

  /**
   * Create a new Followed Project.
   * @return The Followed Project successfully created.
   */
  def create(name: String, pseudo: String): FollowedProject = {
    val mongoProject = MongoDBObject.newBuilder
    mongoProject += "name" -> name
    mongoProject += "pseudo" -> pseudo
    mongoProject += "dirty" -> false

    insert(mongoProject.result)
    FollowedProject(name, pseudo)
  }

  /**
   * Find Projects followed by user.
   * @param User's pseudo.
   * @return Either the founds User Projects, or Nil.
   */
  def byUser(pseudo: String): List[FollowedProject] = {
    val query = MongoDBObject("pseudo" -> pseudo)

    selectOne(query).map { result =>
      FollowedProject(result.getAs[String]("name").get,
                  result.getAs[String]("pseudo").get,
                  result.getAs[Boolean]("dirty").get)
    }.toList
  }

  def dirty(name: String) = {
  }

  implicit val FollowedProjectFormat: Format[FollowedProject] = asProduct3("name", "pseudo", "dirty")(FollowedProject.apply)(FollowedProject.unapply(_).get)
}

case class DebuggedProject(name: String, pseudo: String) extends MongoDB("projects_dg") {
  def sessions = Nil
}
object DebuggedProject extends MongoDB("projects_dg"){

  /**
   * Create a new debugged Project.
   * @return The debugged Project successfully created.
   */
  def create(name: String, pseudo: String): DebuggedProject = {
    val mongoProject = MongoDBObject.newBuilder
    mongoProject += "name" -> name
    mongoProject += "pseudo" -> pseudo

    insert(mongoProject.result)
    DebuggedProject(name, pseudo)
  }

  /**
   * Find Projects debugged by user.
   * @param User's pseudo.
   * @return Either the founds Debugged Projects, or Nil.
   */
  def byUser(pseudo: String): List[DebuggedProject] = {
    val query = MongoDBObject("pseudo" -> pseudo)

    selectOne(query).map { result =>
      DebuggedProject(result.getAs[String]("name").get,
                  result.getAs[String]("pseudo").get)
    }.toList
  }

  implicit val DebuggedProjectFormat: Format[DebuggedProject] = asProduct2("name", "pseudo")(DebuggedProject.apply)(DebuggedProject.unapply(_).get)
}
