package controllers

import play.Logger
import play.api._
import play.api.mvc._
import play.api.data._
import format.Formats._
import validation.Constraints._

import models.{Project, Log, User}
import utils._

object Projects extends Controller with SessionUtils {

  /**
   * View all projects.
   */
  def index() = Action { implicit request =>
    CurrentUser(
      user => Ok(views.html.projects.index(user, projectForm, Project.list())),
      Forbidden
    )
  }

  val projectForm = Form(
    of(
      "name"      -> text,
      "token_pub" -> text
    ) 
  )

  /**
   * Return all projects in json format.
   */
  def projects() = Action {
    Ok//(tojson(Project.list()).toString)
  }

  /**
   * View one project.
   * @param Project name.
   */
  def project(name: String) = Action {
    Project.byName(name).map { project => 
      Ok(views.html.projects.view(project))
    }.getOrElse(NotFound)
  }

  /**
   * Create a project if not exist and add loF entry.
   * @param Project name.
   */
  def addUpLog(name: String) = Action { implicit request =>
    logForm.bindFromRequest.fold(
      errors => BadRequest,
      {
        case (projectName, log) =>
           Project.byName(name).map { project =>
             //project.addUpLog(log)
           }.getOrElse {
             val newProject = Project(name)
             Project.create(newProject)
             //newProject.addUpLog(log)
           }
        Created
      })
  }

  val logForm = Form(
    of(
      "name" -> text,
      "log" -> text
    ) 
  )

  /**
   * List all logs.
   * @param Project name.
   */
  def logs(name: String) = Action {    
    Ok//(tojson(Project(name).logs).toString)
  }
}
