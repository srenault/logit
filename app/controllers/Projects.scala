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
  
  /* List all project */
  def index() = Action {
    Ok(views.html.project.index(Project.list()))
  }

  def getProjects() = Action {
    Ok(tojson(Project.list()).toString)
  }

  /* View one project */
  def view(name: String) = Action {
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

  /* Create a project if not exist (and add log entry) */
  def addLog(name: String) = Action { implicit request =>
    logForm.bindFromRequest.fold(
      errors => BadRequest,
      {
        case (projectName, log) =>
           Project.findByName(name).map { project =>
             project.addLog(log)
           }.getOrElse {
             val newProject = Project(name)
             Project.create(newProject)
             newProject.addLog(log)
           }
        Ok
      })
  }

  /* List all logs of one project */
  def getLogs(name: String) = Action {    
    Ok(tojson(Project(name).logs).toString)
  }
}
