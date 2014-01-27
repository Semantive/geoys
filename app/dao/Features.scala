package dao

import com.vividsolutions.jts.geom.{GeometryFactory, Coordinate, Point}
import utils.pgSlickDriver.simple._
import scala.slick.jdbc.{GetResult, StaticQuery => Q}

import models.Feature

/**
 * Feature table definition.
 *  For more detailed information about rows and stored data, @see{models.Feature}.
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Features extends Table[Feature]("feature") with DAO[Feature] {

  // <editor-fold desc="Row definitions">

  def geonameId     = column[Int]("geoname_id", O.PrimaryKey)
  def defaultName   = column[String]("default_name", O.DBType("VARCHAR(200)"))
  def featureClass  = column[String]("feature_class", O.DBType("CHAR(1)"))
  def featureCode   = column[String]("feature_code", O.DBType("VARCHAR(10)"))
  def admCode       = column[String]("adm_code", O.DBType("VARCHAR(40)"), O.Nullable)
  def countryId     = column[Int]("country_id", O.Nullable)
  def adm1Id        = column[Int]("adm1_id", O.Nullable)
  def adm2Id        = column[Int]("adm2_id", O.Nullable)
  def adm3Id        = column[Int]("adm3_id", O.Nullable)
  def adm4Id        = column[Int]("adm4_id", O.Nullable)
  def parentId      = column[Int]("parent_id", O.Nullable)
  def timezoneId    = column[Int]("timezone_id", O.Nullable)
  def population    = column[Long]("population", O.Nullable)
  def location      = column[Point]("location")
  def wikiLink      = column[String]("wiki_link", O.Nullable)

  // </editor-fold>

  // <editor-fold desc="Foreign keys">

  /** REFERENCES key on country.id. */
  def fkCountry  = foreignKey("fk_feature_country", countryId, Features)(_.geonameId)

  /** REFERENCES key on adm1.id. */
  def fkAdm1     = foreignKey("fk_feature_adm1", adm1Id, Features)(_.geonameId)

  /** REFERENCES key on adm2.id. */
  def fkAdm2     = foreignKey("fk_feature_adm2", adm2Id, Features)(_.geonameId)

  /** REFERENCES key on adm3.id. */
  def fkAdm3     = foreignKey("fk_feature_adm3", adm3Id, Features)(_.geonameId)

  /** REFERENCES key on adm4.id. */
  def fkAdm4     = foreignKey("fk_feature_adm4", adm4Id, Features)(_.geonameId)

  /** REFERENCES key on adm4.id. */
  def fkParentId = foreignKey("fk_feature_parent_id", parentId, Features)(_.geonameId)

  /** REFERENCES key on timezone.id. */
  def fkTimezone = foreignKey("fk_feature_timezone", timezoneId, Timezones)(_.id)

  // </editor-fold>

  // <editor-fold desc="Projections">

  /** Default projection. */
  def * = geonameId ~ defaultName ~ featureClass ~ featureCode ~ admCode.? ~ countryId.? ~ adm1Id.? ~ adm2Id.? ~ adm3Id.? ~ adm4Id.? ~ parentId.? ~ timezoneId.? ~ population.? ~ location ~ wikiLink.? <> (Feature.apply _, Feature.unapply _)

  // </editor-fold>

  // <editor-fold desc="Retrieve methods">

  /**
   *
   * @param geonameId
   * @param lang
   * @param session
   * @return
   */
  def getWithName(geonameId: Int, lang: String)(implicit session: Session): Option[(Feature, Option[String])] =
    matchFeatureWithName(lang).filter(_._1.geonameId === geonameId).firstOption

  /**
   *
   * @param latitude
   * @param longitude
   * @param limit
   * @param session
   * @return
   */
  def getByPoint(latitude: Double, longitude: Double, radius: Double, limit: Int, featureClass: Option[String], featureCode: Option[String])
      (implicit session: Session): List[(Feature, Option[String])] = {

    val geometryFactory = new GeometryFactory()

    val inputPoint = geometryFactory.createPoint(new Coordinate(longitude, latitude))
    val language = "pl"

    val query = (for {
      (f, n) <- joinFeaturesWithNames(language)

      if st_dwithin(f.location, inputPoint, radius)
    } yield (f, n.name.?)).sortBy(tpl => st_distance_sphere(tpl._1.location, inputPoint))


    /* todo: fclass, fcode */

    query.take(limit).list
  }

  /**
   *
   * @param geonameId
   * @param lang
   * @param session
   * @return
   */
  def getChildren(geonameId: Int, lang: String)(implicit session: Session): List[(Feature, Option[String])] =
    matchFeatureWithName(lang).filter(_._1.parentId === geonameId).list

  /**
   * Returns hierarchy of the feature - the feature itself and all of it's parents.
   *
   * @param geonameId id of the feature to search for
   * @param lang      preferred language of the names in the output
   * @return          list of parent features, including given feature
   */
  def getHierarchy(geonameId: Int, lang: String)(implicit session: Session): List[(Feature, Option[String])] = {

    implicit val getFeatureResult = GetResult(r => Feature(r.<<, r.<<, r.<<, r.<<,
      r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

    val query = Q.query[(Int, String), (Feature, Option[String])]("""
    WITH RECURSIVE
      parent_feature(geoname_id, parent_id, depth, path) AS (
          SELECT
            f.geoname_id, f.parent_id, 1::INT AS depth, ARRAY[f.geoname_id] AS path
          FROM
            feature AS f
          WHERE
            f.parent_id IS NULL
         UNION ALL
          SELECT
            f.geoname_id, f.parent_id, pf.depth + 1 AS depth, path || ARRAY[f.geoname_id]
          FROM
            parent_feature AS pf, feature AS f
          WHERE
            f.parent_id = pf.geoname_id
      )
    SELECT feature.*, name_translation.name FROM feature LEFT JOIN name_translation ON feature.geoname_id = name_translation.geoname_id
      WHERE feature.geoname_id = ANY((SELECT path FROM parent_feature AS f WHERE f.geoname_id = ?)::integer[])
      AND name_translation.language = ?
                                                          """)
    query.list((geonameId, lang))
  }

  /**
   *
   * @param geonameId
   * @param lang
   * @param session
   * @return
   */
  def getSiblings(geonameId: Int, lang: String)(implicit session: Session): List[(Feature, Option[String])] = {

    (for {
      p <- Features
      (f, n) <- joinFeaturesWithNames(lang)

      if p.geonameId === geonameId && f.parentId === p.parentId
    } yield (f, n.name.?)).list
  }

  // </editor-fold>

}
