package com.semantive.geoys.tables

/**
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Continent extends BasicFeatureTable[(Option[Int], String, String, Int)]("continent") {
  /** Default projection for SELECT * queries. */
  def * = id.? ~ name ~ asciiName ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = name ~ asciiName ~ geoId
}
