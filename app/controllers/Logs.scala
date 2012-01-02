package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

import models.{Project, Log}

object Logs extends Controller {
  
  /**
   * Create a log entry.
   */
  def create(projectName: String, log: String) = Action {
    Json.parse(log) match {
      case l: JsObject => {
        Project.byName(projectName).map { project =>
          project.addUpLog(l)
        }.getOrElse {
          //val newProject = Project(projectName)
          //newProject.addUpLog(l)
        }
        Created
      }
      case _ => Logger.warn("[Logs] Error while adding up log: " + log); BadRequest
    }
  }
}

/*object JsonValidator {
    def JsonValided[A](jsonString: String, onInvalidFormat: _ => Result)(action: JsValue => Action[A]): Action[(Action[A], A)] {
      
    }
}*/
