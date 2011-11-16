package controllers

import models._

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    val project = Project("NOWT!FY")
    //Project.create(project)
    //println(Project.findByName("Logit").get.name)
    //val log = Log(project.name, Map[String, Any]( ( "message" -> "runtime exception"), ("thread" -> "#1")))
    //Log.create(log)
    //project.logs
    Ok(views.html.index(List(project)))
  }
}
