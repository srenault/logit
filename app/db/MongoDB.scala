package db

import play.Logger
import play.api.{ Configuration, Play }
import play.api.Play.current
import com.mongodb.casbah.Imports._

trait MongoDB {

  val DB_NAME = "logIt"
  
  lazy val db = {
    val config   = Configuration.fromFile(Play.getFile("conf/application.conf"))
    val host     = config.getString("mongo.host").get
    val port     = config.getInt("mongo.port").get
    val dbName   = config.getString("mongo.db").get
    val username = config.getString("mongo.username").getOrElse("")
    val password = config.getString("mongo.password").getOrElse("")

    val co = MongoConnection(host, port)
    val database = co(dbName)
    database.authenticate(username, password)
    database
  }

  def insert(tableName: String, model: MongoDBObject) = {
    play.Logger.error("#############")
    val a = db(tableName) += model
    play.Logger.error("#############")
    a
  }

  def selectAll(tableName: String) = {
    db(tableName).find()
  }

  def selectBy(tableName: String, model: MongoDBObject) = {
    db(tableName).find(model)
  }

  def selectOne(tableName: String, model: MongoDBObject) = {
    db(tableName).findOne(model)
  }
}
