package controllers

import play.api._
import play.api.mvc._

import models.Project

object Projects extends Controller {
  
  /**
   * Create a log entry.
   */
  def index(name: String) = Action {
    Project.findByName(name).map { project => 
      println("###" + project.logs)
      Ok(views.html.project(project))
    }.getOrElse(NotFound)
  }
}
