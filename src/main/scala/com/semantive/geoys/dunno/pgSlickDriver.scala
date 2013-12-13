package com.semantive.geoys.dunno

import slick.driver.PostgresDriver
import com.github.tminglei.slickpg._

/**
 * Driver for slick-pg extension.
 * @author Amadeusz Kosik <akosik@semantive.com>
 * @see https://github.com/tminglei/slick-pg
 */
trait pgSlickDriver extends PostgresDriver
  with PgArraySupport
  with PgRangeSupport
  with PgHStoreSupport
  with PgSearchSupport
  with PostGISSupport {

  override val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {}

  trait ImplicitsPlus extends Implicits
    with ArrayImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with PostGISImplicits

  trait SimpleQLPlus extends SimpleQL
    with ImplicitsPlus
    with SearchAssistants
}

object pgSlickDriver extends pgSlickDriver
