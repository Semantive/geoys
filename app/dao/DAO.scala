package dao

import utils.pgSlickDriver.simple._
import models.AbstractEntity
import com.vividsolutions.jts.geom.{PrecisionModel, GeometryFactory}

trait DAO[T <: AbstractEntity] {
  self: Table[T] =>

  lazy val jtsGeometryFactory = new GeometryFactory(new PrecisionModel(), 4326)

  /**
   * Creates left join of features with translated names in given language.
   *  Very commonly used thus moved to it's own definition
   *
   * @param language  language of preferred names
   * @return          left join of (Feature, Option[Name translation]) tuple
   */
  private[dao] def joinFeaturesWithNames(language: String) =
    Features leftJoin (
      for { nt <- NameTranslations
            if nt.language === language &&
              nt.isOfficial === true
      } yield nt) on (_.geonameId === _.geonameId)  // FixMe: something went wrong here, although it still works...

  /**
   * Joins features with their translated names in given language.
   *
   * @param language  language of preferred names
   * @return          tuple of (Feature, Option[Name translation])
   */
  private[dao] def matchFeatureWithName(language: String) = {
    for {
      (f, n) <- joinFeaturesWithNames(language)
    } yield (f, n.name.?)
  }

  /**
   * Returns list of all items of given type.
   *  Use with caution since geoys operates on quire big amount of rows.
   *
   * @return list of all elements from the table
   */
  def listAll()(implicit session: Session)
    = (for (t <- self) yield t).list
}
