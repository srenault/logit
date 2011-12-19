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
    mongoProject += "debug" -> project.debug

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
      Some(Project(result.getAs[String]("name").get,
                   result.getAs[Boolean]("debug").get))
    }.orElse(None)
  }

  /**
   * Retrieve all Projects.
   * @return Projects list.
   */
  def list(): List[Project] = {
    selectAll(TABLE_NAME).map { result =>
      Project(result.getAs[String]("name").get,
              result.getAs[Boolean]("debug").get)
    }.toList
  }

  implicit val ProjectFormat: Format[Project] = asProduct2("name", "debug")(Project.apply)(Project.unapply(_).get)
}

case class UserProject(name: String, pseudo: String, debug: Boolean = false, dirty: Boolean = false) extends MongoDB
object UserProject extends MongoDB{

  val TABLE_NAME = "user_projects"

  /**
   * Create a new User Project.
   * @return The User Project successfully created.
   */
  def create(project: UserProject): UserProject = {
    val mongoProject = MongoDBObject.newBuilder
    mongoProject += "name" -> project.name
    mongoProject += "pseudo" -> project.pseudo
    mongoProject += "debug" -> project.debug
    mongoProject += "dirty" -> project.debug

    insert(TABLE_NAME, mongoProject.result)
    project
  }

  /**
   * Find Projects followed by user.
   * @param User's pseudo.
   * @return Either the founds User Projects, or Nil.
   */
  def find(pseudo: String, debug: Boolean): List[UserProject] = {
    val query = MongoDBObject("pseudo" -> pseudo,"debug" -> debug)

    selectOne(TABLE_NAME, query).map { result =>
      UserProject(result.getAs[String]("name").get,
                  result.getAs[String]("pseudo").get,
                  result.getAs[Boolean]("debug").get,
                  result.getAs[Boolean]("dirty").get)
    }.toList
  }

  def dirty() = {
    
  }

  implicit val UserProjectFormat: Format[UserProject] = asProduct4("name", "pseudo", "debug", "dirty")(UserProject.apply)(UserProject.unapply(_).get)
}
