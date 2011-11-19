package controllers

import play.api._
import play.api.mvc._
import play.api.data._

import models.Project

object Projects extends Controller {
  
  /* List all project */
  def index() = Action {
    Ok(views.html.project.index(Project.list()))
  }

  /* View one project */
  def view(name: String) = Action {
    Project.findByName(name).map { project => 
      Ok(views.html.project.view(project))
    }.getOrElse(NotFound)
  }

  /* Create a project if not exist */
  def addLog(name: String) = Action {
    val logForm = Form(
      of(
        "log" -> of[String]
      )
    )

    val log = logForm.bindFromRequest.get
    
    Project.findByName(name).map { project =>
      project.addLog(log)
    }.getOrElse {
      val newProject = Project(name)
      newProject.addLog(log)
    }
    Ok
  }

  /* List all logs of one project */
  def getLogs(name: String) = Action {
    import sjson.json._
    import DefaultProtocol._
    import JsonSerialization._

    Ok(tojson(Project(name).logs).toString)
  }
}
