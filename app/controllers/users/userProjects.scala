package controllers.users.projects

import akka.util.duration._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.Comet
import play.api.libs.akka._
import play.api.libs.json._

import play.api._
import play.api.mvc._
import play.api.data._
import validation.Constraints._

import utils.SessionUtils
import models.{User, Project}

object Followed extends Controller with SessionUtils {

  def index(pseudo: String) = Action { implicit request =>
    CurrentUser(
      user => Ok,
      Forbidden
    )
  }

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
  def markAsRead(pseudo: String, name: String, logID: String) = Action {
    Ok
  }
}

object Debugged extends Controller with SessionUtils {
  import models.DebuggedLog
  import models.DebuggedLog._
  import actors.DebugActor
  import actors.DebugActor._

  implicit val DebuggedLogComet = Comet.CometMessage[DebuggedLog](dbLog => toJson(dbLog).toString)

  def index(pseudo: String) = Action { implicit request =>
    CurrentUser(
      user => Ok(views.html.users.debugging(user)),
      Forbidden
    )
  }

  def view(pseudo: String, name: String) = Action { implicit request =>
    /*CurrentUser(
      user => Ok(views.html.users.session(user)),
      Forbidden
    )*/
    Ok(views.html.users.session(User("sre","password", "sre@zenexity.com"), Project("ZEST")))
  }

  def start(pseudo: String, name: String) = Action {
    AsyncResult {
      (DebugActor.ref ? (Listen(pseudo), 5.seconds)).mapTo[Enumerator[DebuggedLog]].asPromise.map { 
        chunks => Ok.stream(chunks &> Comet( callback = "window.parent.session.onReceive"))
      }
    }
  }

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
  def markAsRead(pseudo: String, name: String, logID: String) = Action {
    Ok
  }

  def sendlog() = Action {
    println("heyyyyyy")
    Logger.error("dfsdfdf")
    Ok
  }

  /**
   * Add log for evaluation.
   * @param User's pseudo.
   * @param Project name.
   */
  def eval(pseudo: String, name: String) = ToJsObject { json =>
    DebugActor.ref ! NewLog(pseudo, DebuggedLog(name, "#1", json))
    Ok
  }

  private def ToJsObject(action: JsObject => Result) = Action { implicit request =>
    Logger.info("entering")
    Form(of("data" -> nonEmptyText)).bindFromRequest.fold(
      err => BadRequest("Empty json ?"), {
        case jsonStr => Json.parse(jsonStr) match {
          case jsObj: JsObject => action(jsObj)
          case JsUndefined(error) => Logger.info("BadRequest"); BadRequest("Invalid json: " + error)
          case _ => Logger.info("BadRequest"); BadRequest("Not a json object")
        }
      }
    )
  }
}
