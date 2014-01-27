package dao

import utils.pgSlickDriver.simple._
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

  // <editor-fold desc="Foreign keys">

  /** REFERENCES key on country.id. */
  def fkGeonameId   = foreignKey("fk_translation_geoname", geonameId, Features)(_.geonameId)

  // </editor-fold>

  // <editor-fold desc="Projections">

  /** Default projection. */
  def * = id.? ~ geonameId ~ language ~ name ~ isOfficial <> (NameTranslation.apply _, NameTranslation.unapply _)

  // </editor-fold>


  /**
   *
   * @param needle
   * @param session
   * @return
   */
  def searchFulltext(needle: String)(implicit session: Session): List[(Feature, Option[String])] = {

    implicit val getFeatureResult = GetResult(r => Feature(r.<<, r.<<, r.<<, r.<<,
      r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

    val query = Q.query[(String, String), (Feature, Option[String])]("""
      SELECT feature.*, name_translation.name AS name FROM feature LEFT JOIN name_translation ON feature.geoname_id = name_translation.geoname_id
      WHERE feature.fulltext @@ to_tsquery('english', ?) OR name_translation.fulltext @@ to_tsquery('english', ?)
                                                    """)
    query.list(needle, needle)
  }

}
