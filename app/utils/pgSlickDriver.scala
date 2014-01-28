package utils

import slick.driver.PostgresDriver
import com.github.tminglei.slickpg._
import scala.slick.jdbc.GetResult
import com.vividsolutions.jts.geom.Point

/**
 * Driver for slick-pg extension.
 * @author Amadeusz Kosik <akosik@semantive.com>
 * @author Piotr Jędruszuk <pjedruszuk@semantive.com>

 * @see https://github.com/tminglei/slick-pg
 */
trait pgSlickDriver extends PostgresDriver
  with PgArraySupport
  with PgRangeSupport
  with PgHStoreSupport
  with PgSearchSupport
  with PostGISSupport {

  override val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {
    implicit val pointConverter = GetResult(r => new GeometryTypeMapper[Point].nextValue(r))

    val st_distance_sphere = SimpleFunction.binary[Point, Point, Double]("ST_DISTANCE_SPHERE")
    val st_dwithin = SimpleFunction.ternary[Point, Point, Double, Boolean]("ST_DWITHIN")
  }


  trait ImplicitsPlus extends Implicits
    with ArrayImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with PostGISImplicits

  trait SimpleQLPlus extends SimpleQL
    with ImplicitsPlus
    with SearchAssistants {


  }



}

object pgSlickDriver extends pgSlickDriver
