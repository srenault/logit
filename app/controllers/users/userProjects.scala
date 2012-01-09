package controllers.users.projects

import akka.util.duration._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.Comet
import play.api.libs.akka._
import play.api.libs.json._

import play.api._
import play.api.mvc._

import utils.SessionUtils
import models.User

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
  import actors.DebugActor
  import actors.DebugActor._

  def index(pseudo: String) = Action { implicit request =>
    CurrentUser(
      user => Ok(views.html.users.debugging(user)),
      Forbidden
    )
  }

  def view(pseudo: String, name: String) = Action { implicit request =>
    CurrentUser(
      user => Ok(views.html.users.session(user)),
      Forbidden
    )
  }

  def start(pseudo: String, name: String) = Action {
    AsyncResult {
      (DebugActor.ref ? (Listen(), 5.seconds)).mapTo[Enumerator[String]].asPromise.map { 
        chunks => Ok.stream(chunks &> Comet( callback = "window.parent.trace"))
      }
    }
  }

  /*def start(pseudo: String, name: String) = Action { implicit request =>
    import play.api.libs.iteratee._

    val enumerator = Enumerator(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val enumeratee = Enumeratee.map[Int](integers => (integers * 2).toString + "; ")
    val finalEnumerator = enumerator &> enumeratee

    Ok.stream { socket: Socket.Out[String] =>
      finalEnumerator |>> socket
    }
  }*/

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

  /**
   * Add log for evaluation.
   * @param User's pseudo.
   * @param Project name.
   */
  def eval(pseudo: String, name: String) = Action {
    val log = DebuggedLog("NOWT!FY", "#1", JsObject(Seq.empty))
    DebugActor.ref ! log
    Ok
  }

  def sendlog() = Action {
    val log = DebuggedLog("NOWT!FY", "#1", JsObject(Seq.empty))
    DebugActor.ref ! "hy dude!"
    Ok
  }
}
