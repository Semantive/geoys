package utils.driver

import scala.slick.driver.PostgresDriver
import scala.slick.jdbc.GetResult
import com.vividsolutions.jts.geom._
import scala.slick.lifted._
import scala.slick.ast.Node
import scala.slick.session.PositionedParameters
import com.vividsolutions.jts.io.WKBReader
import java.sql.SQLException
import scala.slick.ast.Library.SqlOperator

/**
 * Driver for slick-pg extension.
 * @author Amadeusz Kosik <akosik@semantive.com>
 * @author Piotr Jędruszuk <pjedruszuk@semantive.com>

 * @see https://github.com/tminglei/slick-pg
 */
trait pgSlickDriver extends PostgresDriver {

  override val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {
    val st_distance_sphere = SimpleFunction.binary[Point, Point, Double]("ST_DISTANCE_SPHERE")
    val st_dwithin = SimpleFunction.ternary[Point, Point, Double, Boolean]("ST_DWITHIN")
  }

  trait ImplicitsPlus extends Implicits
    with pgSlickDriverImplicits

  trait SimpleQLPlus extends SimpleQL
    with ImplicitsPlus {
  }
}

trait pgSlickDriverImplicits {
  implicit val geometryTypeMapper = new GeometryTypeMapper[Geometry]
  implicit val pointTypeMapper = new GeometryTypeMapper[Point]
  implicit val polygonTypeMapper = new GeometryTypeMapper[Polygon]
  implicit val lineStringTypeMapper = new GeometryTypeMapper[LineString]
  implicit val linearRingTypeMapper = new GeometryTypeMapper[LinearRing]
  implicit val geometryCollectionTypeMapper = new GeometryTypeMapper[GeometryCollection]
  implicit val multiPointTypeMapper = new GeometryTypeMapper[MultiPoint]
  implicit val multiPolygonTypeMapper = new GeometryTypeMapper[MultiPolygon]
  implicit val multiLineStringTypeMapper = new GeometryTypeMapper[MultiLineString]

  implicit val pointConverter = GetResult(r => new GeometryTypeMapper[Point].nextValue(r))

  implicit def pointColumnExtensionMethods(c: Column[Point]) = new PointColumnExtensionMethods[Point](c)
}

final class PointColumnExtensionMethods[P1](val c: Column[P1]) extends AnyVal with ExtensionMethods[Point, P1] {
  def <->[P2, R](b: Column[P2])(implicit om: o#arg[Point, P2]#to[Point, R]) = om(new SqlOperator("<->").column(n, Node(b)))
}

object pgSlickDriver extends pgSlickDriver
