package com.semantive.geoys.tables

import com.semantive.geoys.dunno.pgSlickDriver.simple._
import com.vividsolutions.jts.geom.Point

/**
 * Country data table representation.
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Country extends Table[(Option[Int], String, String, String, String, Point, Long, Int)]("country") {
  /** Id generated by the db */
  def id          = column[Int]("id", O.PrimaryKey, O.AutoInc)
  /** ISO code of the country (2 characters). */
  def iso2        = column[String]("iso2", O.DBType("CHAR(2)"))
  /** ISO code of the country (3 characters). */
  def iso3        = column[String]("iso3", O.DBType("CHAR(3)"))
  /** Name of the country. */
  def name        = column[String]("name", O.DBType("VARCHAR(200)"))
  /** Name of the country (ASCII characters only). */
  def asciiName   = column[String]("ascii_name", O.DBType("VARCHAR(200)"))
  /** Location of the country (stored as a point). */
  def location    = column[Point]("location")
  /** Population of the country. */
  def population  = column[Long]("population")
  /** Id of the country in the Geonames db. */
  def geoId       = column[Int]("geo_id")

  /** Default projection for SELECT * queries. */
  def * = id.? ~ iso2 ~ iso3 ~ name ~ asciiName ~ location ~ population ~ geoId

  /** Projection for INSERT queries. */
  def forInsert = iso2 ~ iso3 ~ name ~ asciiName ~ location ~ population ~ geoId

  /** UNIQUE index on iso2. */
  def idxIso2     = index("index_iso_2", iso2, unique = true)
  /** UNIQUE index on iso3. */
  def idxIso3     = index("index_iso_3", iso3, unique = true)
  /** UNIQUE index on geoId. */
  def idxGeoId    = index("index_geo_id", geoId, unique = true)
}