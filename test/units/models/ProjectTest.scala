package tests.units.models

import org.specs2.mutable._

import play.api.test._
import play.api.test.MockApplication._

import models.Project

object ProjectTest extends Specification {

  "Project compagnion object" should {

    "create project with its name" in {
      withApplication(Nil, MockData.dataSource) {
        Project.create(Project("COOL")) must equalTo (Project("COOL"))
      }
    }

    "find project by name" in {
      withApplication(Nil, MockData.dataSource) {
        Project.findByName("COOL") must equalTo (Some(Project("COOL")))
      }
    }

    "list all projects" in {
      withApplication(Nil, MockData.dataSource) {
        Project.list() must equalTo (List(Project("COOL")))
      }
    }
  }
}
