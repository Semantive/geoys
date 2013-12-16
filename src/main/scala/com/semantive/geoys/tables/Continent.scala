package com.semantive.geoys.tables

/**
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Continent extends BasicFeatureTable[(Option[Int], String, String, String, Int)]("continent") {
  /** Continent code. */
  def code        = column[String]("code", O.DBType("CHAR(2)"))

  /** Default projection for SELECT * queries. */
  def * = id.? ~ code ~ name ~ asciiName ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = code ~ name ~ asciiName ~ geoId
}
