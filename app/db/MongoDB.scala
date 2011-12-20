package db

import play.Logger
import play.api.{ Configuration, Play }
import play.api.Play.current
import com.mongodb.casbah.Imports._

class MongoDB(tableName: String) {
  import MongoDB._

  def insert(model: MongoDBObject) = db(tableName) += model

  //def update(tableName: String, key: MongoDBObject, model: MongoDBObject) = db(tableName).findAndModify(key, model)

  //def remove(tableName: String, key: MongoDBObject, model: MongoDBObject) = db(tableName).findAndRemove(key, model)

  def count = db(tableName).count

  def clear() = db(tableName).dropCollection()

  def selectAll() = db(tableName).find()

  def selectBy(model: MongoDBObject) = db(tableName).find(model)

  def selectOne(model: MongoDBObject) = db(tableName).findOne(model)
}

object MongoDB {

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

  def clearAll() = db.dropDatabase()
}
