package dao

import utils.pgSlickDriver.simple._
import models.Country

object Countries extends Table[Country]("country") with DAO[Country] {

  // <editor-fold desc="Row definitions">

  def geonameId       = column[Int]("geoname_id", O.PrimaryKey)
  def iso2Code        = column[String]("iso_2_code", O.DBType("CHAR(2)"))
  def iso3Code        = column[String]("iso_3_code", O.DBType("CHAR(3)"))
  def isoNumeric      = column[String]("iso_numeric", O.DBType("CHAR(3)"))
  def fipsCode        = column[String]("fips_code", O.DBType("CHAR(2)"))
  def population      = column[Long]("population")
  def continentId     = column[Int]("continent_id")
  def topLevelDomain  = column[String]("tld", O.DBType("CHAR(8)"))
  def currencyCode    = column[String]("currency_code", O.DBType("CHAR(3)"))

  // </editor-fold>

  // <editor-fold desc="Foreign keys">

  /** REFERENCES key on continent.id. */
  def fkContinent = foreignKey("fk_country_continent", continentId, Continents)(_.geonameId)

  // </editor-fold>

  // <editor-fold desc="Unique keys">

  /** UNIQUE index on iso2. */
  def idxIso2 = index("uq_country_iso_2", iso2Code, unique = true)

  /** UNIQUE index on iso3. */
  def idxIso3 = index("uq_country_iso_3", iso3Code, unique = true)

  /** UNIQUE index on iso2. */
  def idxIsoNum = index("uq_country_iso_num", isoNumeric, unique = true)

  /** UNIQUE index on iso3. */
  def idxFips = index("uq_country_fips", fipsCode, unique = true)

  // </editor-fold>

  // <editor-fold desc="Projections">

  /** Default projection.. */
  def * = geonameId ~ iso2Code ~ iso3Code ~ isoNumeric ~ fipsCode ~ population ~ continentId ~ topLevelDomain ~ currencyCode <>(Country.apply _, Country.unapply _)

  // </editor-fold>
}
