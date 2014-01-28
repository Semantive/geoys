package dao

import utils.pgSlickDriver.simple._
import models.AbstractEntity
import scala.slick.lifted
import scala.slick.session.Session

trait DAO[T <: AbstractEntity] extends Table[T] {

  def listAll()(implicit session: Session) = (for (t <- this) yield t).list

  def unique(query : Query[DAO[T] with Table[T], T])(implicit session: Session): Option[T] = {
    val list = query.list()
    if (list.size > 1) throw new NonUniqueResult("Non-unique object returned") else if (list.isEmpty) None else Some(list.head)
  }

//  def unique(function : DAO[T] with this.type => lifted.Column[Boolean])(implicit session: Session) ={
//    Query(this).filter((x: DAO[T]) => x match {
//      case y : DAO[T] with DAO.this.type => function.apply(y)
//    }).list()
//  }
//
//  def test = {
//    implicit val session : Session = null
//    Countries.unique(_.fipsCode === "asd")
//  }
}

class NonUniqueResult(message: String) extends RuntimeException(message)

