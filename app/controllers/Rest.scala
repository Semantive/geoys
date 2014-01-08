package controllers

import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json._
import models._
import dao._

/**
 *
 */
object Rest extends Controller {

  // <editor-fold desc="JSON generators">

  private def countryInfoToJson(data: (Country, NameTranslation, Feature)): JsObject = {
    def country = data._1
    def names   = data._2
    def feature = data._3

    Json.obj(
      "name"          -> names.name,
      "geoname_id"    -> country.geonameId,
      "iso2_code"     -> country.iso2Code,
      "iso3_code"     -> country.iso3Code,
      "iso_numeric"   -> country.isoNumeric,
      "fips_code"     -> country.fipsCode,
      "population"    -> country.population,
      "tld"           -> country.topLevelDomain,
      "currency_code" -> country.currencyCode,
      "wiki_link"     -> feature.wikiLink
    )
  }

  // </editor-fold>

  /**
   * Country Info service.
   *
   * @param geonameId geoname id of the country
   * @param lang      language of the result
   * @return          JSON
   */
  def countryInfo(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val country = Countries.getWithName(geonameId, lang)

    if(country.isEmpty)
        NotFound
    else
        Ok(countryInfoToJson(country.get))
  }

}
