package actors

import akka.actor._
import akka.actor.Actor._

import play.api._
import play.api.libs.iteratee._
import play.api.libs.concurrent._

import models.DebuggedLog

class DebugActor extends Actor {
  
  import DebugActor._
  
//  var users = Seq.empty[CallbackEnumerator[DebuggedLog]]
  var users = Seq.empty[CallbackEnumerator[String]]
  
  def receive = {

    case Listen() => {
//      lazy val channel: CallbackEnumerator[DebuggedLog] = new CallbackEnumerator[DebuggedLog](
      lazy val channel: CallbackEnumerator[String] = new CallbackEnumerator[String](
        onComplete = self ! Quit(channel)
      )
      users = users :+ channel
      Logger.info("New debugging session")
      sender ! channel
    }

    case Quit(channel) => {
      Logger.info("Debugging session has been stopped ...")
      users = users.filterNot(_ == channel)
    }
    
//    case l: DebuggedLog => {
    case l: String => {
      Logger.info("Got a log : " + l)
      //users.foreach(_.push(l))
      users.foreach(_.push(l))
    }
  }
}

object DebugActor {

  trait Event
  case class Listen() extends Event
//  case class Quit(channel: CallbackEnumerator[DebuggedLog]) extends Event
  case class Quit(channel: CallbackEnumerator[String]) extends Event
  lazy val system = ActorSystem("debugroom")
  lazy val ref = system.actorOf(Props[DebugActor])
}
