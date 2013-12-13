package com.semantive.geoys.tables

import com.semantive.geoys.dunno.pgSlickDriver.simple._
import com.vividsolutions.jts.geom.Point

/**
 * Country data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Country extends FeatureTable[(Option[Int], String, String, String, String, Point, Long, Int)]("country") {
  /** ISO code of the country (2 characters). */
  def iso2        = column[String]("iso2", O.DBType("CHAR(2)"))
  /** ISO code of the country (3 characters). */
  def iso3        = column[String]("iso3", O.DBType("CHAR(3)"))

  /** Default projection for SELECT * queries. */
  def * = id.? ~ iso2 ~ iso3 ~ name ~ asciiName ~ location ~ population ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = iso2 ~ iso3 ~ name ~ asciiName ~ location ~ population ~ geoId

  /** UNIQUE index on iso2. */
  def idxIso2     = index("uq_country_iso_2", iso2, unique = true)
  /** UNIQUE index on iso3. */
  def idxIso3     = index("uq_country_iso_3", iso3, unique = true)
}