package tests.units.models

import org.specs2.mutable._

import play.api.test._
import play.api.test.MockApplication._

import dispatch.json._

import models.{Log, Project}
import db.MongoDB

object LogTest extends Specification {

  val PROD_PROJECT = "PROD_PROJECT"

  val logData = "{\"logger\" : \"main.Zest\" , \"thread\" : \"main\" , \"date\" : \"2011-11-25 17:56:24,832\" , \"message\" : \"\\nInitialize your variable !\\n\\n\" , \"level\" : \"ERROR\" , \"project\" : \"ZEST\"}"

  "Log compagnion object" should {

    "init database" in {
      withApplication(Nil, MockData.dataSource) {
        MongoDB.clearAll()
      }
    }

    "create log entry" in {
      withApplication(Nil, MockData.dataSource) {
        Log.create(Log(PROD_PROJECT, JsValue.fromString(logData)))
        Log.count must equalTo (1)
      }
    }

    "find logs by project name" in {
      withApplication(Nil, MockData.dataSource) {
        Log.findByProject(PROD_PROJECT).length must equalTo (1)
      }
    }
  }
}
