package com.semantive.geoys.model.entity

import com.semantive.geoys.dunno.pgSlickDriver.simple._

/**
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Timezone extends Table[(Option[Int], Int, String, Float, Float, Float)]("timezone") {
  /** */
  def id          = column[Int]("id", O.PrimaryKey, O.AutoInc)
  /** */
  def countryId   = column[Int]("country_id")
  /** */
  def name        = column[String]("name", O.DBType("VARCHAR(40)"))
  /** */
  def gmtOffset   = column[Float]("gmt_offset", O.DBType("NUMERIC(3, 1)"))
  /** */
  def dstOffset   = column[Float]("dst_offset", O.DBType("NUMERIC(3, 1)"))
  /** */
  def rawOffset   = column[Float]("raw_offset", O.DBType("NUMERIC(3, 1)"))

  /** */
  def * = id.? ~ countryId ~ name ~ gmtOffset ~ dstOffset ~ rawOffset
  /** */
  def forInsert = countryId ~ name ~ gmtOffset ~ dstOffset ~ rawOffset
}
