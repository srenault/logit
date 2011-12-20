package tests.units.models

import org.specs2.mutable._

import play.api.test._
import play.api.test.MockApplication._

import dispatch.json._

import models.{Project, UserProject, Log}
import db.MongoDB

object ProjectTest extends Specification {

  val PROD_PROJECT = "PROD_PROJECT"

  val logData = "{\"logger\" : \"main.Zest\" , \"thread\" : \"main\" , \"date\" : \"2011-11-25 17:56:24,832\" , \"message\" : \"\\nInitialize your variable !\\n\\n\" , \"level\" : \"ERROR\" , \"project\" : \"ZEST\"}"


  "Project compagnion object" should {

    "init database" in {
      withApplication(Nil, MockData.dataSource) {
        MongoDB.clearAll()
      }
    }

    "create project with its name" in {
      withApplication(Nil, MockData.dataSource) {
        Project.create(Project(PROD_PROJECT)) must equalTo (Project(PROD_PROJECT))
      }
    }

    "find a project by name" in {
      withApplication(Nil, MockData.dataSource) {
        Project.findByName(PROD_PROJECT) must equalTo (Some(Project(PROD_PROJECT)))
      }
    }

    "list all projects" in {
      withApplication(Nil, MockData.dataSource) {
        Project.list() must equalTo (List(Project(PROD_PROJECT)))
      }
    }

    "add log to the project" in {
      withApplication(Nil, MockData.dataSource) {
        Project(PROD_PROJECT).addUpLog(logData)
      }
    }

    "retrieve logs project" in {
      withApplication(Nil, MockData.dataSource) {
        Project(PROD_PROJECT).logs.length must equalTo (1)
      }
    }
  }
}

object UserProjectTest extends Specification {

  "init database" in {
    withApplication(Nil, MockData.dataSource) {
      MongoDB.clearAll()
    }
  }
 
  "UserProject companion object" should {
    "make to dirty a user project when a new log is added" in {
      withApplication(Nil, MockData.dataSource) {
        UserProject.dirty("COOL")
      }
    }
  }
}
