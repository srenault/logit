package models

import com.mongodb.casbah.Imports._
import sjson.json._
import DefaultProtocol._
import JsonSerialization._
import dispatch.json._

import db.MongoDB

case class User(pseudo: String, email: String, password: String) {

  /**
   * Retrieve user's projects.
   * @return Projects list.
   */
  def projects: List[Project] = Nil

  /**
   * Followed Projects by user.
   * @return List of followed Projects.
   */
  def followedProjects: List[Project] = Nil

  /**
   * Debugged Projects by user.
   * @return List of debugged Projects.
   */
  def debuggedProjects: List[Project] = Nil
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

    val result = selectOne(TABLE_NAME, query)
    (for { 
      r <- result
      pseudo <- r.getAs[String]("pseudo")
      email  <- r.getAs[String]("email")
      pwd    <- r.getAs[String]("password")
    } yield(User(pseudo, email, pwd))).orElse(None)
  }

  /**
   * Create a new User.
   * @param New User.
   * @return The User successfully created.
   */
  def create(newUser: User): User = {
    val mongoUser = MongoDBObject.newBuilder
    mongoUser += "pseudo" -> newUser.pseudo
    mongoUser += "email" -> newUser.email
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
      email  <- r.getAs[String]("email")
      pwd    <- r.getAs[String]("password")
    } yield(User(pseudo, email, pwd))).orElse(None)
  }
}
