package com.semantive.geoys.tables

import com.vividsolutions.jts.geom.Point

/**
 * ADM1 (the second highest administration division) data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object ADM2 extends FeatureTable[(Option[Int], String, String, Option[Point], Int, String, Int, Option[Long], Option[Int], Int)]("adm2") {
  /** Id of the country ADM belongs to. */
  def countryId   = column[Int]("country_id")
  /** ID of administrative division (some geonames invention). */
  def admCode     = column[String]("adm_code", O.DBType("VARCHAR(20)"))
  /** Id of the parent ADM1. */
  def adm1Id      = column[Int]("adm1_id")
  /** Id of the timezone. */
  def timezoneId  = column[Int]("timezone_id")

  /** Default projection for SELECT * queries. */
  def * = id.? ~ name ~ asciiName ~ location.? ~ countryId ~ admCode ~ adm1Id ~ population.? ~ timezoneId.? ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = name ~ asciiName ~ location.? ~ countryId ~ admCode ~ adm1Id ~ population.? ~ timezoneId.? ~ geoId

  /** REFERENCES key on country.id. */
  def fkAdm2Country = foreignKey("fk_" + tableName +  "_country", countryId, Country)(_.id)
  /** REFERENCES key on adm1.id. */
  def fkAdm2Adm1 = foreignKey("fk_" + tableName +  "_adm1", adm1Id, ADM1)(_.id)
  /** REFERENCES key on timezone.id. */
  def fkAdm1Timezone = foreignKey("fk_" + tableName + "_timezone", timezoneId, Timezone)(_.id)
}
