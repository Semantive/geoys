package com.semantive.geoys.tables

import com.vividsolutions.jts.geom.Point

/**
 * ADM1 (the highest administration division) data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object ADM1 extends FeatureTable[(Option[Int], String, String, Option[Point], Int, String, Option[Long], Option[Int], Int)]("adm1") {
  /** Id of the country ADM belongs to. */
  def countryId   = column[Int]("country_id")
  /** Id of the timezone. */
  def timezoneId  = column[Int]("timezone_id")
  /** ID of administrative division (some geonames invention). */
  def admCode     = column[String]("adm_code", O.DBType("VARCHAR(40)"))

  /** Default projection for SELECT * queries. */
  def * = id.? ~ name ~ asciiName ~ location.? ~ countryId ~ admCode ~ population.? ~ timezoneId.? ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = name ~ asciiName ~ location.? ~ countryId ~ admCode ~ population.? ~ timezoneId.? ~ geoId

  /** REFERENCES key on country.id. */
  def fkAdm1Country = foreignKey("fk_" + tableName +  "_country", countryId, Country)(_.id)
  /** REFERENCES key on timezone.id. */
  def fkAdm1Timezone = foreignKey("fk_" + tableName + "_timezone", timezoneId, Timezone)(_.id)
}
