package controllers.Rest

import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current

import dao._
import utils.JsonTemplates

/**
 * REST service controller for Reverse Geocoding search services.
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object ReverseGeocoding extends Controller {

  /**
   * CountryCode service.
   *  Returns ISO2 code of the country given location is in.
   *
   * @param longitude longitude of the location
   * @param latitude  latitude of the location
   * @return          JSON
   */
  def countryCode(longitude: Double, latitude: Double) = DBAction { implicit rs =>

    val countryCode = Countries.getByPoint(longitude, latitude)

    if(countryCode == None)
      NotFound
    else
      Ok(countryCode.get)
  }

  /**
   * FindNearby service.
   *  Returns list of features closest to the given point.
   *
   * @todo heavily under construction - define rest of the required/optional parameters
   *
   * @param longitude longitude of the location
   * @param latitude  latitude of the location
   * @param limit     max number of results
   * @return          JSON
   */
  def findNearby(longitude: Double, latitude: Double, radius: Double, limit: Int, featureClass: Option[String], featureCode: Option[String], countryBias: Option[String]) = DBAction { implicit rs =>

    val features = Features.getByPoint(longitude, latitude, radius, limit, featureClass, featureCode, countryBias)

    if(features.length == 0)
      NotFound
    else
      Ok(JsonTemplates.findNearbyToJson(features))
  }
}
