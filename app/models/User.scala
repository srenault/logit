package models

import com.mongodb.casbah.Imports._
import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

import db.MongoDB

case class User(pseudo: String, password: String) {

  /**
   * Retrieve user's projects.
   * Projects list.
   */
  def projects: List[UserProject] = Nil
}

object User extends MongoDB {

  val TABLE_NAME = "users" /** User table name. */

  /**
   * Authenticate a User.
   * @param User's pseudo.
   * @param User's password.
   * @return Either the found User, or None.
   */
  def authenticate(pseudo: String, password: String): Option[User] = {
    val query  = MongoDBObject("pseudo"   -> pseudo,
                               "password" -> password)

    selectOne(TABLE_NAME, query).flatMap{
      r => Some(User(pseudo, password))
    }.orElse(None)
  }

  /**
   * Create a new User.
   * @param New User.
   * @return The User successfully created.
   */
  def create(newUser: User): User = {
    val mongoUser = MongoDBObject.newBuilder
    mongoUser += "pseudo" -> newUser.pseudo
    mongoUser += "password" -> newUser.password

    insert(TABLE_NAME, mongoUser.result)
    newUser
  }

  /**
   * Find a User by pseudo.
   * @param User's pseudo.
   * @return Either the found User, or None.
   */
  def findByPseudo(pseudo: String): Option[User] = {
    val query  = MongoDBObject("pseudo" -> pseudo)
    val result = selectOne(TABLE_NAME, query)
    
    (for { 
      r <- result
      pseudo <- r.getAs[String]("pseudo")
      pwd    <- r.getAs[String]("password")
    } yield(User(pseudo, pwd))).orElse(None)
  }
}

case class UserProject() {
  def logs: List[UserLog] = Nil
}
object UserProject {
}

case class UserLog()
object UserLog {
}
