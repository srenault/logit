package actors

import akka.actor._
import akka.actor.Actor._

import play.api._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import models.DebuggedLog

class DebugActor extends Actor {
  
  import DebugActor._
  
  var users = Map.empty[String, CallbackEnumerator[DebuggedLog]]
  
  def receive = {

    case Listen(pseudo) => {
      lazy val channel: CallbackEnumerator[DebuggedLog] = new CallbackEnumerator[DebuggedLog](
        onComplete = self ! Quit(pseudo)
      )
      users = users + (pseudo -> channel)
      Logger.info("New debugging session")
      sender ! channel
    }

    case Quit(pseudo) => {
      Logger.info("Debugging session has been stopped ...")
      users = users.filterNot(c => c._1 == pseudo)
    }
    
    case NewLog(pseudo, log) => {
      Logger.info("Got a log : " + log)
      users.find(u => u._1 == pseudo).map(u => u._2.push(log))
    }
  }
}

object DebugActor {

  trait Event
  case class Listen(pseudo: String) extends Event
  case class Quit(pseudo: String) extends Event
  case class NewLog(pseudo: String, log: DebuggedLog)
  lazy val system = ActorSystem("debugroom")
  lazy val ref = system.actorOf(Props[DebugActor])
}
