package com.semantive.geoys.model.entity

import com.vividsolutions.jts.geom.Point

/**
 * Country data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Country extends FeatureTable[(Option[Int], String, String, String, String, String, Option[Point], Int, Long, Int)]("country") {
  /** ISO code of the country (2 characters). */
  def iso2        = column[String]("iso2", O.DBType("CHAR(2)"))
  /** ISO code of the country (3 characters). */
  def iso3        = column[String]("iso3", O.DBType("CHAR(3)"))
  /** FIPS code of the country (2 characters). */
  def fips        = column[String]("fips", O.DBType("CHAR(2)"))
  /** Id of the parent continent. */
  def continentId = column[Int]("continent_id")

  /** Default projection for SELECT * queries. */
  def * = id.? ~ iso2 ~ iso3 ~ fips ~ name ~ asciiName ~ location.? ~ continentId ~ population ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = iso2 ~ iso3 ~ fips ~ name ~ asciiName ~ location.? ~ continentId ~ population ~ geoId

  /** UNIQUE index on iso2. */
  def idxIso2     = index("uq_country_iso_2", iso2, unique = true)
  /** UNIQUE index on iso3. */
  def idxIso3     = index("uq_country_iso_3", iso3, unique = true)
}