package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import format.Formats._
import validation.Constraints._

import models.{User, Project}

object Application extends Controller {

  /**
   * Home page.
   */
  def index() = Action {
    Ok(views.html.index(signupForm))
  }

  /**
   * Create a new user.
   */
  def signup() = Action { implicit request =>
    signupForm.bindFromRequest.fold(
      errors => Ok(views.html.index(signupForm)),
      {
        case u: User => 
          User.create(u)
          Ok(views.html.index(signupForm))
      })
  }

  
  val signupForm = Form(
    of(User.apply _, User.unapply _) (
      "pseudo"   -> of[String].verifying(required),
      "email"    -> of[String].verifying(required),
      "password" -> of[String].verifying(required)
    )
  )

  /**
   * Authenticate user.
   */
  def signin() = Action { implicit request =>
    signinForm.bindFromRequest.fold(
      errors => Ok(views.html.index(signupForm)),
      {
        case (pseudo, password) => 
          User.authenticate(pseudo, password).map { u =>
            Ok(views.html.project.index(u, Project.list()))
          }.getOrElse{
            play.Logger.debug("hey")
            Ok(views.html.index(signupForm))
          }
      })
  }

  val signinForm = Form(
    of(
      "pseudo" -> text,
      "password" -> text
    ) 
  )

  /**
   * Log Out user.
   */
  def logout() = Action {
    Ok
  }
}

