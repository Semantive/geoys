package com.semantive.alpha.mock

import com.semantive.geoys.dunno.pgSlickDriver.simple._
import com.semantive.geoys.tables._
import com.vividsolutions.jts.geom._
import scala.slick.session.Database.threadLocalSession

/**
 * Mock object, used only for tests.
 */
object MockDbSpammer
{
  def createDDL(dbSession: Database): Unit = {
    dbSession withSession {
      Country.ddl.create
      ADM1.ddl.create
      ADM2.ddl.create
    }
  }

  def dropDDL(dbSession: Database): Unit = {
    dbSession withSession {
      ADM2.ddl.drop
      ADM1.ddl.drop
      Country.ddl.drop
    }
  }

  def insertMockData(dbSession: Database): Unit = {
    dbSession withSession {
      Country.forInsert.insert("TK", "KIE", "Kielce", "Kielce", new Point(new Coordinate(20.62752, 50.87033), new PrecisionModel(), 4326), 260, 5)
      Country.forInsert.insert("WF", "WXW", "Warszawa", "Warszawa", new Point(new Coordinate(21.01178, 52.22977), new PrecisionModel(), 4326), 260, 6)

      Query(Country).foreach(println)
    }
  }

  def main(arguments: Array[String]): Unit =
  {
    implicit val dbSession = Database.forURL("jdbc:postgresql://127.0.0.1/geoys", user = "geoys", password = "-----", driver = "org.postgresql.Driver")

    createDDL(dbSession)
    insertMockData(dbSession)
    dropDDL(dbSession)
  }
}