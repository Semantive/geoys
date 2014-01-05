package dao

import utils.pgSlickDriver.simple._
import models.AbstractEntity

trait DAO[T <: AbstractEntity] {
  self: Table[T] =>

  def listAll()(implicit session: Session)
    = (for (t <- self) yield t).list
}
