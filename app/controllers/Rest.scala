package controllers

import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json._
import models._
import dao.Countries

object Rest extends Controller {

  /* FixMe: find a better place for this function. */
  private def countryToJson(data: (Country, NameTranslation)): JsObject = {
    def country = data._1
    def names   = data._2

    Json.obj(
      "name"          -> names.name,
      "geoname_id"    -> country.geonameId,
      "iso2_code"     -> country.iso2Code,
      "iso3_code"     -> country.iso3Code,
      "iso_numeric"   -> country.isoNumeric,
      "fips_code"     -> country.fipsCode,
      "population"    -> country.population,
      "tld"           -> country.topLevelDomain,
      "currency_code" -> country.currencyCode
    )
  }

  def countryInfo(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val country = Countries.getWithName(geonameId, lang)

    if(country.isEmpty)
        NotFound
    else
        Ok(countryToJson(country.get))
  }

}
