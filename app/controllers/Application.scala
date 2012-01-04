package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Security
import play.api.data._
import format.Formats._
import validation.Constraints._

import models.{User, Project}
import controllers.users._

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
          Redirect(routes.Application.index())
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
            play.Logger.debug("Authentication successful. Redirecting to user home page...")
            Redirect("/dashboard").withSession(session + (Security.username -> u.pseudo)) //TODO
          }.getOrElse{
            Redirect("/dashboard")//routes.Users.indexroutes.Application.index())
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

