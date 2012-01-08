package controllers.users

import play.api._
import play.api.mvc._
import play.api.mvc.Security._

import models.Project
import utils.SessionUtils

object Users extends Controller with SessionUtils {

  /**
   * User's dashboard.
   * @param User's pseudo.
   */
  def index() = Action { implicit request =>
    CurrentUser(
      user => Ok(views.html.users.index(user)),
      Forbidden
    )
  }

  /**
   * User want to follow to a new project.
   * @param User's pseudo.
   * @param Project name.
   */
  def follow(pseudo: String, name: String) = {
    Authenticated { _ =>
      Action {
        Ok
      }
    }
  }

  /**
   * User want to debug to a new project.
   * @param User's pseudo.
   * @param Project name.
   */
  def debug(pseudo: String, name: String) = {
    Authenticated { _ =>
      Action {
        Ok
      }
    }
  }
}
