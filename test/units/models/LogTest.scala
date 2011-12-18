package tests.units.models

import org.specs2.mutable._

import play.api.test._
import play.api.test.MockApplication._

import models.Log

object LogTest extends Specification {

val logData = """[{\"logger\" : \"main.Zest\" , \"thread\" : \"main\" , \"date\" : \"2011-11-25 17:56:24,832\" , \"message\" : \"\\nInitialize your variable !\\n\\n\" , \"level\" : \"ERROR\" , \"project\" : \"ZEST\"}"}, {"projectName" : "ZEST", "dataJSON" : "{ \"_id\" : { \"$oid\" : \"4ecfc8fc43b8626f1dac9936\"} , \"logger\" : \"main.Zest\" , \"thread\" : \"main\" , \"date\" : \"2011-11-25 17:57:32,092\" , \"message\" : \"\\nInitialize your variable !\\n\\n\" , \"level\" : \"ERROR\" , \"project\" : \"COOL\"}\"}]"""

  "Log compagnion object" should {

    "create log entry" in {
      withApplication(Nil, MockData.dataSource) {
        Log.create(Log("litig", "pwd")) must equalTo (Log("COOL", logData))
      }
    }

    "find logs by project name" in {
      withApplication(Nil, MockData.dataSource) {
        Log.findByProject("COOL") must equalTo (List(Log("COOL", logData)))
      }
    }
  }
}
