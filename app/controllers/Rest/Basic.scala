package controllers.Rest

import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current

import dao._
import utils.JsonTemplates

/**
 * REST service controller for basic services (i.e. services which requests Geoname ID and returns detailed data).
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Basic extends Controller {

  /**
   * CountryInfo service.
   *  Returns detailed information about given country.
   *
   * @param geonameId Geoname ID of the country
   * @param lang      preferred language of the result
   * @return          JSON
   */
  def countryInfoByGeoId(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val country = Countries.getByGeoIdWithName(geonameId, lang)

    if(country.isEmpty)
      NotFound
    else
      Ok(JsonTemplates.countryInfoToJson(country.get))
  }

  /**
   * CountryInfo service.
   *  Returns detailed information about given country.
   *
   * @param iso2Code  ISO 2-alpha code of the country
   * @param lang      language of the result
   * @return          JSON
   */
  def countryInfoByIso(iso2Code: String, lang: String) = DBAction { implicit rs =>

    val country = Countries.getByIsoWithName(iso2Code, lang)

    if(country.isEmpty)
      NotFound
    else
      Ok(JsonTemplates.countryInfoToJson(country.get))
  }

  /**
   * FeatureInfo service.
   *  Returns detailed information about given feature.
   *
   * @param geonameId Geoname ID of the feature
   * @param lang      preferred language of the result
   * @return          JSON
   */
  def featureInfo(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val feature = Features.getWithName(geonameId, lang)

    if(feature.isEmpty)
      NotFound
    else
      Ok(JsonTemplates.featureInfoToJson(feature.get))
  }
}
