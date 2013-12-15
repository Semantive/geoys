package com.semantive.geoys.tables

import com.semantive.geoys.dunno.pgSlickDriver.simple._
import com.vividsolutions.jts.geom.Point

/**
 * ADM1 (the highest administration division) data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object ADM1 extends FeatureTable[(Option[Int], String, String, Point, Int, Int, Long, Int)]("ADM1") {
  /** Id of the country ADM belongs to. */
  def countryId   = column[Int]("country_id")
  /** ID of administrative division (some geonames invention). */
  def admId       = column[Int]("adm_id")

  /** Default projection for SELECT * queries. */
  def * = id.? ~ name ~ asciiName ~ location ~ countryId ~ admId ~ population ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = name ~ asciiName ~ location ~ countryId ~ admId ~ population ~ geoId

  /** REFERENCES key on country.id. */
  def fkAdm1Country = foreignKey("fk_" + tableName +  "_country", countryId, Country)(_.id)
}
