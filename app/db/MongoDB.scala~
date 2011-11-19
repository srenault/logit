package db

import com.mongodb.casbah.Imports._

trait MongoDB {

  val DB_NAME = "logIt"
  
  lazy val db = {
    val mongoConnection = MongoConnection()
    mongoConnection(DB_NAME)
  }

  def insert(tableName: String, model: MongoDBObject) = {
    db(tableName) += model
  }

  def select(tableName: String, model: MongoDBObject) = {
    db(tableName).find(model)
  }

  def selectOne(tableName: String, model: MongoDBObject) = {
    db(tableName).findOne(model)
  }
}
