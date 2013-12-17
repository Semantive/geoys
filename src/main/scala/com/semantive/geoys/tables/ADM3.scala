package com.semantive.geoys.tables

import com.vividsolutions.jts.geom.Point

/**
 * ADM3 (the third highest administration division) data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object ADM3 extends FeatureTable[(Option[Int], String, String, Point, Int, String, Int, Int, Long, Option[Int], Int)]("adm3") {
  /** Id of the country ADM belongs to. */
  def countryId   = column[Int]("country_id")
  /** ID of administrative division (some geonames invention). */
  def admCode     = column[String]("adm_code", O.DBType("VARCHAR(20)"))
  /** Id of the parent ADM1. */
  def adm1Id      = column[Int]("adm1_id")
  /** Id of the parent ADM2. */
  def adm2Id      = column[Int]("adm2_id")
  /** Id of the timezone. */
  def timezoneId  = column[Int]("timezone_id")

  /** Default projection for SELECT * queries. */
  def * = id.? ~ name ~ asciiName ~ location ~ countryId ~ admCode ~ adm1Id ~ adm2Id ~ population ~ timezoneId.? ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = name ~ asciiName ~ location ~ countryId ~ admCode ~ adm1Id ~ adm2Id ~ population ~ timezoneId.? ~ geoId

  /** REFERENCES key on country.id. */
  def fkAdm3Country = foreignKey("fk_" + tableName +  "_country", countryId, Country)(_.id)
  /** REFERENCES key on adm1.id. */
  def fkAdm3Adm1 = foreignKey("fk_" + tableName +  "_adm1", adm1Id, ADM1)(_.id)
  /** REFERENCES key on adm2.id. */
  def fkAdm3Adm2 = foreignKey("fk_" + tableName +  "_adm2", adm2Id, ADM2)(_.id)
  /** REFERENCES key on timezone.id. */
  def fkAdm1Timezone = foreignKey("fk_" + tableName + "_timezone", timezoneId, Timezone)(_.id)
}
