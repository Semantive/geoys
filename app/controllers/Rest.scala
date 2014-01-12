package controllers

import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current

import dao._
import utils.JsonTemplates

/**
 *
 */
object Rest extends Controller {

  def children(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val features = Features.getChildren(geonameId, lang)

    if(features.length == 0)
      NotFound
    else
      Ok(JsonTemplates.childrenToJson(features))
  }

  /**
   * Country Info service.
   *
   * @param geonameId geoname id of the country
   * @param lang      language of the result
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
   * Country Info service.
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

  def featureInfo(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val feature = Features.getByGeoIdWithName(geonameId, lang)

    if(feature.isEmpty)
      NotFound
    else
      Ok(JsonTemplates.featureInfoToJson(feature.get))
  }

  def siblings(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val features = Features.getSiblings(geonameId, lang)

    if(features.length == 0)
      NotFound
    else
      Ok(JsonTemplates.siblingsToJson(features))
  }
}
