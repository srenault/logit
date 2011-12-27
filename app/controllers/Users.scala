package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Security._

import models.Project

object Users extends Controller {

  /**
   * User's dashboard.
   * @param User's pseudo.
   */
  def index() = {
    Authenticated { _ =>
      Action { implicit request =>
        Application.currentUser.map { u =>
          Ok(views.html.users.dashboard(u))
        }.getOrElse(Forbidden)
      }
    }
  }

  /**
   * Retrieve all projects that current user follow.
   * @param User's pseudo.
   */
  def projects(pseudo: String) = {
    Authenticated { _ =>
      Action {
        Ok
      }
    }
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
   * Retrieve one specific project that current user follows.
   * @param User's pseudo.
   * @param Project name.
   */
  def project(pseudo: String, name: String) = Action {
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

  /**
   * Retrieve all stream logs.
   * @param User's pseudo.
   * @param Project name.
   */
  def reader(pseudo: String, name: String) = Action {
    Ok
  }
}
