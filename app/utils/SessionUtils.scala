package utils

import play.api._
import play.api.mvc._

import models.User

trait SessionUtils {
  self: Controller =>

  def CurrentUser(success: User => Result, failed: Result)(implicit request: RequestHeader): Result = {
    session.get(Security.username).map { pseudo =>
      User.byPseudo(pseudo).map(u => success(u)).getOrElse(failed)
    }.getOrElse(failed)
  }
}
