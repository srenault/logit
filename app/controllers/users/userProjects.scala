package controllers.users.projects

import play.api._
import play.api.mvc._

import utils.SessionUtils
import models.User

object Followed extends Controller with SessionUtils {

  /**
   * Retrieve all projects that current user follow.
   * @param User's pseudo.
   */
  def list(pseudo: String) = Action { implicit request =>
    CurrentUser(
      user => Ok,
      Forbidden
    )
  }

  /**
   * Retrieve one specific project that current user follows.
   * @param User's pseudo.
   * @param Project name.
   */
  def view(pseudo: String, name: String) = Action {
    Ok
  }

  /**
   * Retrieve all logs of specified project for the current user.
   * @param User's pseudo.
   * @param Project name.
   */
  def logs(pseudo: String, name: String) = Action {
    Ok
  }

  /**
   * Mark one log as read.
   * @param User's pseudo.
   * @param Project name.
   * @param Log id.
   */
  def readLog(pseudo: String, name: String, logID: String) = Action {
    Ok
  }
}

object Debugged extends Controller {

  /**
   * Retrieve all projects that current user debugs.
   * @param User's pseudo.
   */
  def list(pseudo: String) = {
    Action {
      Ok
    }
  }

  /**
   * Retrieve one specific project that current user debugs.
   * @param User's pseudo.
   * @param Project name.
   */
  def view(pseudo: String, name: String) = Action {
    Ok
  }

  /**
   * Retrieve all logs of specified project for the current user.
   * @param User's pseudo.
   * @param Project name.
   */
  def logs(pseudo: String, name: String) = Action {
    Ok
  }

  /**
   * Mark one log as read.
   * @param User's pseudo.
   * @param Project name.
   * @param Log id.
   */
  def readLog(pseudo: String, name: String, logID: String) = Action {
    Ok
  }

  /**
   * Add log for evaluation.
   * @param User's pseudo.
   * @param Project name.
   */
  def eval(pseudo: String, name: String) = Action {
    Ok
  }
}
