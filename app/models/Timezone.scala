package models

import play.api.Play.current
import utils.pgSlickDriver.simple._

case class Timezone(id: Option[Long], countryId: Long, name: String, gmtOffset: Double, dstOffset: Double, rawOffset: Double)

trait TimezoneComponent {

  class Timezones extends Table[Timezone]("timezone") {
    /** */
    def id          = column[Long]("id", O.PrimaryKey, O.AutoInc)
    /** */
    def countryId   = column[Long]("country_id")
    /** */
    def name        = column[String]("name", O.DBType("VARCHAR(40)"))
    /** */
    def gmtOffset   = column[Double]("gmt_offset", O.DBType("NUMERIC(3, 1)"))
    /** */
    def dstOffset   = column[Double]("dst_offset", O.DBType("NUMERIC(3, 1)"))
    /** */
    def rawOffset   = column[Double]("raw_offset", O.DBType("NUMERIC(3, 1)"))

    /** */
    def * = id.? ~ countryId ~ name ~ gmtOffset ~ dstOffset ~ rawOffset <> (Timezone.apply _, Timezone.unapply _)
    /** */
    def autoInc = * returning id
  }
}

object Timezones extends TimezoneComponent {

  val Timezones = new Timezones

  def insert(timezone: Timezone)(implicit session: Session) {
    Timezones.autoInc.insert(timezone)
  }
}