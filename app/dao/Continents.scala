package dao

import utils.pgSlickDriver.simple._
import models.Continent

/**
 * Continent table definition.
 *  For more detailed information about rows and stored data, @see{models.Continent}.
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Continents extends Table[Continent]("continent") with DAO[Continent] {

  // <editor-fold desc="Row definitions">

  def geonameId = column[Long]("geoname_id", O.PrimaryKey)
  def code = column[String]("code", O.DBType("CHAR(2)"))

  // </editor-fold>

  // <editor-fold desc="Projections">

  /** Default projection.. */
  def * = geoId ~ code <>(Continent.apply _, Continent.unapply _)

  // </editor-fold>
}
