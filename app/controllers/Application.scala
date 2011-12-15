package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import format.Formats._
import validation.Constraints._

import models.User

object Application extends Controller {

  /**
   * Home page.
   */
  def index() = Action {
    Ok(views.html.index())
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
    Ok(views.html.index("Hy dude"))
  }

  val signinForm = Form(
    of(User.apply _, User.unapply _) (
      "pseudo"   -> of[String].verifying(required),
      "password" -> of[String].verifying(required)
    )
  )

  /**
   * Log Out user.
   */
  def logout() = Action {
    Ok
  }
}

