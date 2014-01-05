package dao

import utils.pgSlickDriver.simple._
import com.vividsolutions.jts.geom.Point
import models.Country

object Countries extends Table[Country]("country") with DAO[Country] {


  /** Name of the country. */
  def name = column[String]("name", O.DBType("VARCHAR(200)"))

  /** Name of the country (ASCII characters only). */
  def asciiName = column[String]("ascii_name", O.DBType("VARCHAR(200)"))

  /** ISO code of the country (2 characters). */
  def iso2 = column[String]("iso2", O.DBType("CHAR(2)"))

  /** ISO code of the country (3 characters). */
  def iso3 = column[String]("iso3", O.DBType("CHAR(3)"))

  /** FIPS code of the country (2 characters). */
  def fips = column[String]("fips", O.DBType("CHAR(2)"))

  /** Location of the country (stored as a point). */
  def location = column[Point]("location")

  /** Id of the parent continent. */
  def continentId = column[Long]("continent_id")

  /** Population of the country. */
  def population = column[Long]("population")

  /** Id of the continent in the Geonames db. */
  def geonameId = column[Int("geoname_id")

  /** REFERENCES key on continent.id. */
  def fkContinent = foreignKey("fk_country_continent", continentId, Continents)(_.id)

  /** UNIQUE index on iso2. */
  def idxIso2 = index("uq_country_iso_2", iso2, unique = true)

  /** UNIQUE index on iso3. */
  def idxIso3 = index("uq_country_iso_3", iso3, unique = true)

  /** UNIQUE index on geoId. */
  def idxGeoId = index("uq_country_geo_id", geoId, unique = true)

  /** Default projection.. */
  def * = id.? ~ name ~ asciiName ~ iso2 ~ iso3 ~ fips ~ location.? ~ continentId ~ population ~ geoId <>(Country.apply _, Country.unapply _)
}
