package dao

import utils.driver.pgSlickDriver.simple._
import models.{Feature, NameTranslation}
import scala.slick.jdbc.{StaticQuery => Q, GetResult}

/**
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object NameTranslations extends Table[NameTranslation]("name_translation") with DAO[NameTranslation] {

  // <editor-fold desc="Row definitions">

  def id            = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def geonameId     = column[Int]("geoname_id")
  def language      = column[String]("language", O.DBType("CHAR(8)"))
  def name          = column[String]("name")
  def isOfficial    = column[Boolean]("is_official")

  // </editor-fold>

  // <editor-fold desc="Projections">

  /** Default projection. */
  def * = geonameId ~ language ~ name ~ isOfficial <> (NameTranslation.apply _, NameTranslation.unapply _)

  // </editor-fold>

  /**
   *
   * @param needle
   * @param session
   * @return
   */
  def searchFulltext(needle: String, lang: String, featureClass: Option[String], featureCode: Option[String], countryBias: Option[String], limit: Option[Int])
    (implicit session: Session): List[(Feature, Option[String])] = {

    implicit val getFeatureResult = GetResult(r => Feature(r.<<, r.<<, r.<<, r.<<,
      r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

    /* Following code is in some way bad, but it is waiting for types for lifted queries. */

    var queryString = "SELECT feature.geoname_id, feature.default_name, feature.feature_class, feature.feature_code, feature.adm_code," +
      " feature.country_id, feature.adm1_id, feature.adm2_id, feature.adm3_id, feature.adm4_id, feature.parent_id, feature.timezone_id," +
      " feature.population, feature.location, feature.wiki_link, name_translation.name AS name FROM (feature LEFT JOIN country ON feature.country_id = country.geoname_id) " +
      " LEFT JOIN name_translation ON feature.geoname_id = name_translation.geoname_id " +
      " WHERE feature.fulltext @@ to_tsquery('english', ?) OR name_translation.fulltext @@ to_tsquery('english', ?) "

    if(countryBias.isDefined)
      queryString = queryString + " ORDER BY country.iso2_code <> ? "

    if(limit.isDefined)
      queryString = queryString + " LIMIT " + limit.get.toString

    if(countryBias.isDefined)
      Q.query[(String, String, String), (Feature, Option[String])](queryString).list(needle, needle, countryBias.get)
    else
      Q.query[(String, String), (Feature, Option[String])](queryString).list(needle, needle)
  }

}
