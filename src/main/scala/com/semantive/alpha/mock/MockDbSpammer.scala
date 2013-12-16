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
  private implicit val dbSession = Database.forURL("jdbc:postgresql://127.0.0.1/geoys", user = "geoys", password = "---", driver = "org.postgresql.Driver")

  /**
   * Filters out non-data (e.g. comments) from the geonames data source file.
   *
   * @param line
   * @return
   */
  private def sourceFileLineFilter(line: String): Boolean = {
    if(line(0) == '#' || line(0) == ' ')
      false
    else
      line.split("\t")(16) != ""
  }

  /**
   * Inserts continents' data into the database.
   *  Since the continents are rather not changing anytime soon, their data is hardcoded.
   */
  def insertContinents(): Unit = {
    dbSession withSession {
      Continent.forInsert.insert("Africa", "Africa", 6255146)
      Continent.forInsert.insert("Asia", "Asia", 6255147)
      Continent.forInsert.insert("Europe", "Europe", 6255148)
      Continent.forInsert.insert("North America", "North America", 6255149)
      Continent.forInsert.insert("South America", "South America", 6255150)
      Continent.forInsert.insert("Oceania", "Oceania", 6255151)
      Continent.forInsert.insert("Antarctica", "Antarctica", 6255152)
    }
  }

  /**
   * Inserts countries' data into the database.
   *  Uses countryInfo.txt and allCountries.txt from the geonames dump.
   */
  def insertCountries(): Unit = {
    // 1: countryInfo
    val countriesRawFile = io.Source.fromFile("C:\\Geonames\\countryInfo.txt")
    countriesRawFile.getLines().filter(sourceFileLineFilter).foreach(insertBasicCountryLine)
    countriesRawFile.close()

    // 2: allCountries
    val allCountriesRawFile = io.Source.fromFile("C:\\Geonames\\allCountries.txt")
    allCountriesRawFile.getLines().filter(sourceFileLineFilter).filter(line => line.split("\t")(7).length >= 3 && line.split("\t")(7).substring(0, 3) == "PCL").foreach(insertDetailedCountryLine)
    allCountriesRawFile.close()
  }

  // <editor-fold desc="Country helpers">

  private def insertBasicCountryLine(lineStr: String): Unit = {
    def line = lineStr.split("\t")

    dbSession withSession {
      // FixMe: continent id
      Country.forInsert.insert(line(0), line(1), line(4), line(4), new Point(new Coordinate(0, 0), new PrecisionModel(), 4326), 0, line(7).toLong, line(16).toInt)
    }
  }

  private def insertDetailedCountryLine(line: String): Unit = {
    dbSession withSession {
      val q = for { c <- Country if c.geoId === line.split("\t")(0).toInt } yield c.location
      q.update(new Point(new Coordinate(line.split("\t")(5).toFloat, line.split("\t")(4).toFloat), new PrecisionModel(), 4326))
    }
  }

  // </editor-fold>

  def main(arguments: Array[String]): Unit =
  {
//    insertContinents()
//    insertCountries()
  }
}