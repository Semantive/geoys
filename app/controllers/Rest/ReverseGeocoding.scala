package controllers.Rest

import play.api.mvc._

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
  def countryCode(longitude: Double, latitude: Double) = TODO

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
  def findNearby(longitude: Double, latitude: Double, limit: Int) = TODO
}
