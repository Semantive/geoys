package com.semantive.geoys.model.entity

import com.semantive.geoys.dunno.pgSlickDriver.simple._
import com.vividsolutions.jts.geom.Point

/**
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
abstract class FeatureTable[T](tableName: String) extends BasicFeatureTable[T](tableName) {
  /** Location of the country (stored as a point). */
  def location    = column[Point]("location")
  /** Population of the country. */
  def population  = column[Long]("population")
}
