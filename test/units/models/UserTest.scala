package tests.units.models

import org.specs2.mutable._

import play.api.test._
import play.api.test.MockApplication._

import models.{User, Project, UserProject}
import db.MongoDB

object UserTest extends Specification {

  "User compagnion object" should {

    "init database" in {
      withApplication(Nil, MockData.dataSource) {
        MongoDB.clearAll()
      }
    }

    "create user with username & password" in {
      withApplication(Nil, MockData.dataSource) {
        User.create(User("litig", "sre@zenexity.com", "pwd")) must equalTo (User("litig", "sre@zenexity.com", "pwd"))
      }
    }

    "find user by pseudo" in {
      withApplication(Nil, MockData.dataSource) {
        User.findByPseudo("litig") must equalTo (Some(User("litig", "sre@zenexity.com", "pwd")))
      }
    }

    "authenticate user by username & password" in {
      withApplication(Nil, MockData.dataSource) {
        User.authenticate("litig", "pwd") must equalTo (Some(User("litig", "sre@zenexity.com", "pwd")))
      }
    }

    "follow a prod projects" in {
      withApplication(Nil, MockData.dataSource) {
        User("litig", "sre@zenexity.com", "pwd").followProject("COOL")
      }
    }

    "retrieve followed prod projects" in {
      withApplication(Nil, MockData.dataSource) {
        User("litig", "sre@zenexity.com", "pwd").followedProjects must equalTo (List(UserProject("COOL", "litig")))
      }
    }

    "debug any projects" in {
      withApplication(Nil, MockData.dataSource) {
        User("litig", "sre@zenexity.com", "pwd").debugProject("COOL_DEBUG")
      }
    }

    "retrieve debugged projects" in {
      withApplication(Nil, MockData.dataSource) {
        User("litig", "sre@zenexity.com", "pwd").debuggedProjects must equalTo (List(UserProject("COOL_DEBUG", "litig", true)))
      }
    }
  }
}
