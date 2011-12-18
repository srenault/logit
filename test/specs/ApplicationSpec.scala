package tests.specs

import org.specs2.mutable._

import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.test._
import play.api.test.MockApplication._
import play.api.mvc._

class SigninRequest[AnyContent] extends Request[AnyContent] {
  def uri = "/signin"
  def method = "POST"
  def queryString = Map.empty()
  def body: AnyContent = AnyContentAsUrlFormEncoded(Map("pseudo"->Seq("litig"),
                                                        "password" -> Seq("password"))).asInstanceOf[AnyContent]
  def path ="/signin"
  def headers = new Headers {
    def getAll(key: String) = Nil
    def keys = Set.empty
  }
  def cookies = new Cookies {
      def get(name: String) = None
  }
}

object SigninSpec extends Specification {

  "the application " should {
    "redirect a user to his home page when credentials are valid" in {
        //withApplication(Nil, MockData.dataSource) {
          val action = controllers.Application.signin()
          val result = action.apply(new SigninRequest)
          val extracted = Extract.from(result)
          extracted._1.toString must equalTo("200")
          extracted._2.toString must equalTo("Map(Content-Type -> text/html; charset=utf-8)")
          extracted._3 must contain ("Hy dude")
        //}
    }
  }
}
