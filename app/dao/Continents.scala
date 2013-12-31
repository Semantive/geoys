package dao

import utils.pgSlickDriver.simple._
import models.Continent

object Continents extends Table[Continent]("continent") with DAO[Continent] {
  /** Id generated by the db */
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  /** Name of the country. */
  def name = column[String]("name", O.DBType("VARCHAR(200)"))

  /** Name of the country (ASCII characters only). */
  def asciiName = column[String]("ascii_name", O.DBType("VARCHAR(200)"))

  /** Continent code. */
  def code = column[String]("code", O.DBType("CHAR(2)"))

  /** Id of the continent in the Geonames db. */
  def geoId = column[Long]("geo_id")

  /** UNIQUE index on geoId. */
  def idxGeoId = index("uq_continent_geo_id", geoId, unique = true)

  /** Default projection.. */
  def * = id.? ~ name ~ asciiName ~ code ~ geoId <>(Continent.apply _, Continent.unapply _)
}