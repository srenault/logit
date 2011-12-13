package controllers

import models._

import play.api._
import play.api.mvc._

object Application extends Controller {

  /**
   * Home page.
   */
  def index = Action {
    Ok
  }

  /**
   * Create a new user.
   */
  def signup() = Action {
    Ok
  }

  /**
   * Authenticate user.
   */
  def signin() = Action {
    Ok
  }

  /**
   * Log Out user.
   */
  def logout() = Action {
    Ok
  }
}
