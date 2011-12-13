package controllers

import play.api._
import play.api.mvc._

object Users extends Controller {

  /**
   * Retrieve all projects that current user follow.
   * @param User's pseudo.
   */
  def projects(pseudo: String) = Action {
    Ok
  }

  /**
   * User want to follow to an new project.
   * @param User's pseudo.
   * @param Project name.
   */
  def follow(pseudo: String, name: String) = Action {
    Ok
  }

  /**
   * Retrieve one specific project that current user follow.
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
