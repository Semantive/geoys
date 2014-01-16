package utils

import com.vividsolutions.jts.geom.Point

import models._
import play.api.libs.json._

/**
 * Helper for JSON generation. Stores templates for REST responses.
 */
object JsonTemplates {

  /**
   * Projection of CountryInfo REST service.
   *
   * @param data data of the requested country from the model
   * @return     JSON response object
   */
  def countryInfoToJson(data: (Country, Option[String], Feature)): JsObject = {
    def country = data._1
    def names   = data._2
    def feature = data._3

    def name      = if(! names.isEmpty) names.get else feature.defaultName

    Json.obj("country" -> Json.obj(
        "name"          -> name,
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
    )
  }

  /**
   * Projection of FeatureInfo REST service and component of all other services returning collection of the features.
   *
   * @param data data of the requested feature from the model
   * @return     JSON response object
   */
  def featureInfoToJson(data: (Feature, Option[String])): JsObject = {
    def feature = data._1
    def names   = data._2

    /* Defined explicitly for clarification. */
    def name      = if(! names.isEmpty) names.get else feature.defaultName

    Json.obj(
      "feature" -> Json.obj(
        "name"          -> name,
        "geoname_id"    -> feature.geonameId,
        "latitude"      -> feature.latitude,
        "longitude"     -> feature.longitude,
        "population"    -> feature.population,
        "wiki_link"     -> feature.wikiLink
      )
    )
  }

  /**
   * Projection of Children REST service.
   *
   * @param data list of features received from the model
   * @return     JSON response object
   */
  def childrenToJson(data: List[(Feature, Option[String])]): JsObject = {
    Json.obj(
      "children" -> data.map {child => featureInfoToJson(child)}
    )
  }

  /**
   * Projection of Hierarchy REST service.
   *
   * @todo update so it can show all data of the retrieved features (@see dao.Features).
   *
   * @param data  list of features received from the model
   * @return      JSON response object
   */
  def hierarchyToJson(data: List[(Int)]): JsObject = {
    Json.obj(
      "hierarchy" -> data.map {child => Json.obj("geoname_id" -> child)}
    )
  }

  /**
   * Projection of Siblings REST service.
   *
   * @param data list of features received from the model
   * @return     JSON response object
   */
  def siblingsToJson(data: List[(Feature, Option[String])]): JsObject = {
    Json.obj(
      "siblings" -> data.map {sibling => featureInfoToJson(sibling)}
    )
  }
}
