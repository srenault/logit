package controllers

import play.Logger
import play.api._
import play.api.mvc._
import play.api.data._

import sjson.json._
import DefaultProtocol._
import JsonSerialization._
  
import models.{Project, Log}

object Projects extends Controller {

  /**
   * View all projects.
   */
  def projects() = Action {
    Ok(tojson(Project.list()).toString)
  }

  /**
   * View one project.
   * @param Project name.
   */
  def project(name: String) = Action {
    Project.findByName(name).map { project => 
      Ok(views.html.project.view(project))
    }.getOrElse(NotFound)
  }

  val logForm = Form(
    of(
      "name" -> text,
      "log" -> text
    ) 
  )

  /**
   * Create a project if not exist and add log entry.
   * @param Project name.
   */
  def addUpLog(name: String) = Action { implicit request =>
    logForm.bindFromRequest.fold(
      errors => BadRequest,
      {
        case (projectName, log) =>
           Project.findByName(name).map { project =>
             project.addUpLog(log)
           }.getOrElse {
             val newProject = Project(name)
             Project.create(newProject)
             newProject.addUpLog(log)
           }
        Ok
      })
  }

  /**
   * List all logs.
   * @param Project name.
   */
  def logs(name: String) = Action {    
    Ok(tojson(Project(name).logs).toString)
  }
}
