package dao

import com.vividsolutions.jts.geom.Point
import utils.pgSlickDriver.simple._
import models.GeoFeature

/**
 * GeoFeature table definition.
 */
object GeoFeatures extends Table[GeoFeature]("feature") with DAO[GeoFeature] {
  /** Id generated by the db */
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  /** Name of the feature. */
  def name = column[String]("name", O.DBType("VARCHAR(200)"))

  /** Name of the feature (ASCII characters only). */
  def asciiName = column[String]("ascii_name", O.DBType("VARCHAR(200)"))

  /** Feature class. */
  def fClass = column[String]("f_class", O.DBType("CHAR(1)"))

  /** Feature code. */
  def fCode = column[String]("f_code", O.DBType("VARCHAR(10)"))

  /** ID of administrative division (some geonames invention). */
  def admCode = column[String]("adm_code", O.DBType("VARCHAR(40)"), O.Nullable)

  /** Id of the timezone. */
  def timezoneId = column[Long]("timezone_id", O.Nullable)

  /** Location of the feature (stored as a point). */
  def location = column[Point]("location", O.Nullable)

  /** Id of the country that the feature belongs to. */
  def countryId = column[Long]("country_id")

  /** Id of the parent ADM1. */
  def adm1Id = column[Long]("adm1_id", O.Nullable)

  /** Id of the parent ADM2. */
  def adm2Id = column[Long]("adm2_id", O.Nullable)

  /** Id of the parent ADM3. */
  def adm3Id = column[Long]("adm3_id", O.Nullable)

  /** Id of the parent ADM4. */
  def adm4Id = column[Long]("adm4_id", O.Nullable)

  /** Id of the parent feautre. */
  def parentId = column[Long]("parent_id", O.Nullable)

  /** Population. */
  def population = column[Long]("population", O.Nullable)

  /** Id in the Geonames db. */
  def geoId = column[Long]("geo_id")

  /** Level of the feature: 1..4 for ADM1..4,  5 for {PPL, PPLC, PPLA}, 6..9 for PPL1..4, 10 for PPLX. */
  def level = column[Int]("level")

  //<editor-fold desc="Foreign keys">

  /** REFERENCES key on country.id. */
  def fkCountry  = foreignKey("fk_feature_country", countryId, Countries)(_.id)

  /** REFERENCES key on adm1.id. */
  def fkAdm1     = foreignKey("fk_feature_adm1", adm1Id, GeoFeatures)(_.id)

  /** REFERENCES key on adm2.id. */
  def fkAdm2     = foreignKey("fk_feature_adm2", adm2Id, GeoFeatures)(_.id)

  /** REFERENCES key on adm3.id. */
  def fkAdm3     = foreignKey("fk_feature_adm3", adm3Id, GeoFeatures)(_.id)

  /** REFERENCES key on adm4.id. */
  def fkAdm4     = foreignKey("fk_feature_adm4", adm4Id, GeoFeatures)(_.id)

  /** REFERENCES key on adm4.id. */
  def fkParentId = foreignKey("fk_feature_parent_id", parentId, GeoFeatures)(_.id)

  /** REFERENCES key on timezone.id. */
  def fkTimezone = foreignKey("fk_feature_timezone", timezoneId, Timezones)(_.id)

  //</editor-fold>

  /** UNIQUE index on geoId. */
  def idxGeoId = index("uq_featuure_geo_id", (geoId, level), unique = true)

  /** Default projection.. */
  def * = id.? ~ name ~ asciiName ~ fClass ~ fCode ~ admCode.? ~ timezoneId.? ~ location.? ~ countryId ~ population.? ~ geoId ~ adm1Id.? ~ adm2Id.? ~ adm3Id.? ~ adm4Id.? ~ parentId.? ~ level <>(GeoFeature.apply _, GeoFeature.unapply _)
}