package utils

import com.vividsolutions.jts.geom.Point

import models._
import play.api.libs.json._

/**
 * Helper for JSON generation. Stores templates for REST responses.
 */
object JsonTemplates {

  /**
   * Extracts longitude and latitude from JTS Point.
   *
   * @param point point of the feature (JTS Point)
   * @return      point's coordinates in format (longitude, latitude)
   */
  private def extractCoordinates(point: Option[Point]): (Option[Double], Option[Double]) = {

    if(point == None)
      (None, None)
    else
      (Option.apply(point.get.getX()),Option.apply(point.get.getY()))
  }

  /**
   * Projection of CountryInfo REST service.
   *
   * @param data data of the requested country from the model
   * @return     JSON response object
   */
  def countryInfoToJson(data: (Country, NameTranslation, Feature)): JsObject = {
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

  /**
   * Projection of FeatureInfo REST service and component of all other services returning collection of the features.
   *
   * @param data data of the requested feature from the model
   * @return     JSON response object
   */
  def featureInfoToJson(data: (Feature, NameTranslation)): JsObject = {
    def feature = data._1
    def names   = data._2

    def coordinates = extractCoordinates(feature.location)
    /* Defined explicitly for clarification. */
    def longitude = coordinates._1
    def latitude  = coordinates._2

    Json.obj(
      "name"          -> names.name,
      "geoname_id"    -> feature.geonameId,
      "latitude"      -> latitude,
      "longitude"     -> longitude,
      "population"    -> feature.population,
      "wiki_link"     -> feature.wikiLink
    )
  }

  /**
   * Projection of Children REST service.
   *
   * @param data data of the requested country from the model
   * @return     JSON response object.
   */
  def childrenToJson(data: List[(Feature, NameTranslation)]): JsObject = {
    Json.obj(
      "children" -> data.map {child => featureInfoToJson(child)}
    )
  }

  /**
   * Projection of Siblings REST service.
   *
   * @param data data of the requested country from the model
   * @return     JSON response object.
   */
  def siblingsToJson(data: List[(Feature, NameTranslation)]): JsObject = {
    Json.obj(
      "siblings" -> data.map {sibling => featureInfoToJson(sibling)}
    )
  }
}
