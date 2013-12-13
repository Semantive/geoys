package com.semantive.geoys.tables

import com.vividsolutions.jts.geom.Point

/**
 * ADM1 (the second highest administration division) data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object ADM2 extends FeatureTable[(Option[Int], String, String, Point, Int, Int, Long, Int)]("ADM2") {
  /** Id of the country ADM belongs to. */
  def countryId   = column[Int]("country_id")
  /** Id of the parent ADM1. */
  def adm1Id      = column[Int]("adm1_id")

  /** Default projection for SELECT * queries. */
  def * = id.? ~ name ~ asciiName ~ location ~ countryId ~ adm1Id ~ population ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = name ~ asciiName ~ location ~ countryId ~ adm1Id ~ population ~ geoId

  /** REFERENCES key on country.id. */
  def fkAdm2Country = foreignKey("fk_" + tableName +  "_country", countryId, Country)(_.id)
  /** REFERENCES key on adm1.id. */
  def fkAdm2Adm1 = foreignKey("fk_" + tableName +  "_adm1", adm1Id, ADM1)(_.id)
}
