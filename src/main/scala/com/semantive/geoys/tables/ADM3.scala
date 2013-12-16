package com.semantive.geoys.tables

import com.vividsolutions.jts.geom.Point

/**
 * ADM3 (the third highest administration division) data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object ADM3 extends FeatureTable[(Option[Int], String, String, Point, Int, Int, Int, Int, Long, Int)]("ADM3") {
  /** Id of the country ADM belongs to. */
  def countryId   = column[Int]("country_id")
  /** ID of administrative division (some geonames invention). */
  def admId       = column[Int]("adm_id")
  /** Id of the parent ADM1. */
  def adm1Id      = column[Int]("adm1_id")
  /** Id of the parent ADM2. */
  def adm2Id      = column[Int]("adm2_id")

  /** Default projection for SELECT * queries. */
  def * = id.? ~ name ~ asciiName ~ location ~ countryId ~ admId ~ adm1Id ~ adm2Id ~ population ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = name ~ asciiName ~ location ~ countryId ~ admId ~ adm1Id ~ adm2Id ~ population ~ geoId

  /** REFERENCES key on country.id. */
  def fkAdm3Country = foreignKey("fk_" + tableName +  "_country", countryId, Country)(_.id)
  /** REFERENCES key on adm1.id. */
  def fkAdm3Adm1 = foreignKey("fk_" + tableName +  "_adm1", adm1Id, ADM1)(_.id)
  /** REFERENCES key on adm2.id. */
  def fkAdm3Adm2 = foreignKey("fk_" + tableName +  "_adm2", adm2Id, ADM2)(_.id)
}