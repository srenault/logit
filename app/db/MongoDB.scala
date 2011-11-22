package db

import play.Logger
import com.mongodb.casbah.Imports._

trait MongoDB {

  val DB_NAME = "logIt"
  
  lazy val db = MongoConnection()(DB_NAME)

  def insert(tableName: String, model: MongoDBObject) = {
    db(tableName) += model
  }

  def selectAll(tableName: String) = {
    db(tableName).find()
  }

  def select(tableName: String, model: MongoDBObject) = {
    db(tableName).find(model)
  }

  def selectOne(tableName: String, model: MongoDBObject) = {
    db(tableName).findOne(model)
  }
}
