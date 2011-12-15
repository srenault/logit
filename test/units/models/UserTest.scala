package tests.units.models

import org.specs2.mutable._

import play.api.test._
import play.api.test.MockApplication._

import models.User

object UserTest extends Specification {

  "User compagnion object" should {

    "create user with username & password" in {
      withApplication(Nil, MockData.dataSource) {
        User.create(User("litig", "pwd")) must equalTo (User("litig", "pwd"))
      }
    }

    "find user by username" in {
      withApplication(Nil, MockData.dataSource) {
        User.findByPseudo("litig") must equalTo (Some(User("litig", "pwd")))
      }
    }

    "authenticate user by username & password" in {
      withApplication(Nil, MockData.dataSource) {
        User.authenticate("litig", "pwd") must equalTo (Some(User("litig", "pwd")))
      }
    }
  }
}
