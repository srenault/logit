package controllers

import play.api._
import play.api.mvc._
import models.{ Project, Log }

object Logs extends Controller {
  
  /**
   * Create a log entry.
   */
  def create(projectName: String, log: String) = Action {
    Project.findByName(projectName).map { project =>
      project.addUpLog(log)
    }.getOrElse {
      val newProject = Project(projectName)
      newProject.addUpLog(log)
    }
    Ok
  }
}
