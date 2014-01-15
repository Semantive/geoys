package services

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

/**
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
@RunWith(classOf[JUnitRunner])
class ChildrenServiceSpec extends Specification {

  /** Hardcoded geoname ID of the Earth. */
  val GEOID_EARTH = 6295630

  /** Hardcoded geoname IDs of the continents. */
  val GEOID_CONINENTS = List[Int](6255146, 6255151, 6255147, 6255148, 6255150, 6255149, 6255152)

  "Children service" should {

    "retrieve children of the Earth (continents)" in new WithApplication {
      val result = controllers.Rest.Hierarchy.children(GEOID_EARTH, "")(FakeRequest())

      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")

      val geoysJsonResponse: JsValue = Json.parse(contentAsString(result))

      val childrensIds = geoysJsonResponse \ "children" \\ "geoname_id"
      childrensIds.foreach(x => {println(x); GEOID_CONINENTS must contain(x.as[Int])})
    }
  }
}