package dao

import com.vividsolutions.jts.geom.Point
import utils.pgSlickDriver.simple._
import scala.slick.jdbc.{GetResult, StaticQuery => Q}

import models.{NameTranslation, Feature}

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
  def location      = column[Point]("location", O.Nullable)
  def population    = column[Long]("population", O.Nullable)
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
  def * = geonameId ~ defaultName ~ featureClass ~ featureCode ~ admCode.? ~ countryId.? ~ adm1Id.? ~ adm2Id.? ~ adm3Id.? ~ adm4Id.? ~ parentId.? ~ timezoneId.? ~ location.? ~ population.? ~ wikiLink.? <> (Feature.apply _, Feature.unapply _)

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
   * @todo return (Feature, NameTranslation) tuple instead of geonameId only. Problems:
   *        # creating Feature case class requires 14 of r.<< - better solution preferred
   *        # no implicit conversion from PostGIS Point (DB) to JTS Point (for case class)
   *
   * @param geonameId id of the feature to search for
   * @param lang      preferred language of the names in the output
   * @return          list of parent features, including given feature
   */
  def getHierarchy(geonameId: Int, lang: String)(implicit session: Session): List[(Int)] = {

    val query = Q.query[(Int), (Int)]("""
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
    SELECT geoname_id FROM feature WHERE geoname_id = ANY((SELECT path FROM parent_feature AS f WHERE f.geoname_id = ?)::integer[])
                                                   """)
    query.list(geonameId)
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
