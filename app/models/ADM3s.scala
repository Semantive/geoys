package models

/**
 * Created by piotrjedruszuk on 31.12.2013.
 */
class ADM3s extends Table[ADM3]("adm3") {
  /** Id generated by the db */
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  /** Name of the country. */
  def name = column[String]("name", O.DBType("VARCHAR(200)"))

  /** Name of the country (ASCII characters only). */
  def asciiName = column[String]("ascii_name", O.DBType("VARCHAR(200)"))

  /** ID of administrative division (some geonames invention). */
  def admCode = column[String]("adm_code", O.DBType("VARCHAR(40)"))

  /** Id of the timezone. */
  def timezoneId = column[Long]("timezone_id")

  /** Location of the country (stored as a point). */
  def location = column[Point]("location")

  /** Id of the country ADM belongs to. */
  def countryId = column[Long]("country_id")

  /** Id of the parent ADM1. */
  def adm1Id = column[Long]("adm1_id")

  /** Id of the parent ADM2. */
  def adm2Id = column[Long]("adm2_id")

  /** Population of the adm. */
  def population = column[Long]("population")

  /** Id of the continent in the Geonames db. */
  def geoId = column[Long]("geo_id")

  /** REFERENCES key on country.id. */
  def fkCountry = foreignKey("fk_adm3_country", countryId, Countries)(_.id)

  /** REFERENCES key on adm1.id. */
  def fkAdm1 = foreignKey("fk_adm3_adm1", adm1Id, ADM1s)(_.id)

  /** REFERENCES key on adm1.id. */
  def fkAdm2 = foreignKey("fk_adm3_adm2", adm2Id, ADM2s)(_.id)

  /** REFERENCES key on timezone.id. */
  def fkTimezone = foreignKey("fk_adm3_timezone", timezoneId, Timezones)(_.id)

  /** UNIQUE index on geoId. */
  def idxGeoId = index("uq_adm3_geo_id", geoId, unique = true)

  /** Default projection.. */
  def * = id.? ~ name ~ asciiName ~ admCode ~ timezoneId.? ~ location.? ~ countryId ~ adm1Id ~ adm2Id ~ population.? ~ geoId <>(ADM3.apply _, ADM3.unapply _)

  /** */
  def autoInc = * returning id
}
