package dao

import scala.Predef._
import utils.pgSlickDriver.simple._
import models.Timezone

object Timezones extends Table[Timezone]("timezone") with DAO[Timezone] {
  /** */
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  /** */
  def countryId = column[Long]("country_id")

  /** */
  def name = column[String]("name", O.DBType("VARCHAR(40)"))

  /** */
  def gmtOffset = column[Double]("gmt_offset", O.DBType("NUMERIC(3, 1)"))

  /** */
  def dstOffset = column[Double]("dst_offset", O.DBType("NUMERIC(3, 1)"))

  /** */
  def rawOffset = column[Double]("raw_offset", O.DBType("NUMERIC(3, 1)"))

  /** REFERENCES key on country.id. */
  def fkContinent = foreignKey("fk_timezone_country", countryId, Countries)(_.id)

  /** UNIQUE index on name. */
  def idxName = index("uq_timezone_name", name, unique = true)

  /** */
  def * = id.? ~ countryId ~ name ~ gmtOffset ~ dstOffset ~ rawOffset <>(Timezone.apply _, Timezone.unapply _)
}