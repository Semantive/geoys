package dao

import scala.Predef._
import utils.pgSlickDriver.simple._
import models.Timezone

/**
 * Timezone table definition.
 *  For more detailed information about rows and stored data, @see{models.Timezone}.
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Timezones extends Table[Timezone]("timezone") with DAO[Timezone] {

  // <editor-fold desc="Row definitions">

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def countryId = column[Int]("country_id")
  def name = column[String]("name", O.DBType("VARCHAR(40)"))
  def gmtOffset = column[Double]("gmt_offset", O.DBType("NUMERIC(3, 1)"))
  def dstOffset = column[Double]("dst_offset", O.DBType("NUMERIC(3, 1)"))
  def rawOffset = column[Double]("raw_offset", O.DBType("NUMERIC(3, 1)"))

  // </editor-fold>

  // <editor-fold desc="Foreign keys">

  /** REFERENCES key on country.id. */
  def fkContinent = foreignKey("fk_timezone_country", countryId, Countries)(_.geonameId)

  // </editor-fold>

  // <editor-fold desc="Unique keys">

  /** UNIQUE index on name. */
  def idxName = index("uq_timezone_name", name, unique = true)

  // </editor-fold>

  // <editor-fold desc="Projections">

  /** Default projection. */
  def * = id.? ~ countryId ~ name ~ gmtOffset ~ dstOffset ~ rawOffset <>(Timezone.apply _, Timezone.unapply _)

  /** Projection for insertion */
  def insertion = countryId ~ name ~ gmtOffset ~ dstOffset ~ rawOffset <>(t => new Timezone(t._1, t._2, t._3, t._4, t._5) , (t : Timezone) => Some((t.countryId, t.name, t.gmtOffset, t.dstOffset, t.rawOffset)))

  // </editor-fold>
}