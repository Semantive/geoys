package dao

import utils.pgSlickDriver.simple._
import models.NameTranslation

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
  def * = geonameId ~ language ~ name ~ isOfficial <> (NameTranslation.apply _, NameTranslation.unapply _)

  // </editor-fold>

}
