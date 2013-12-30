package utils

import slick.driver.PostgresDriver
import com.github.tminglei.slickpg._

/**
 * Driver for slick-pg extension.
 * @author Amadeusz Kosik <akosik@semantive.com>
 * @see https://github.com/tminglei/slick-pg
 */
trait pgSlickDriver extends PostgresDriver
  with PostGISSupport {

  override val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {}

  trait ImplicitsPlus extends Implicits
    with PostGISImplicits

  trait SimpleQLPlus extends SimpleQL
}

object pgSlickDriver extends pgSlickDriver
