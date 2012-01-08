package actors

import akka.actor._
import akka.actor.Actor._

import play.api._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import models.DebuggedLog

class DebugActor extends Actor {
  
  import DebugActor._
  
  var users = Seq.empty[CallbackEnumerator[DebuggedLog]]
  
  def receive = {
    
    case Quit(channel) => {
      Logger.info("Member has disconnected: " + channel)
      users = users.filterNot(_ == channel)
    }
    
    case l: DebuggedLog => {
      Logger.info("Got log")
      users.foreach(_.push(l))
    }
  }
}

object DebugActor {

  trait Event
  case class Quit(channel: CallbackEnumerator[String]) extends Event
  lazy val system = ActorSystem("debugroom")
  lazy val ref = system.actorOf(Props[DebugActor])
}
