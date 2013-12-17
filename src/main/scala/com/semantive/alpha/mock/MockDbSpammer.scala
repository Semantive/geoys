package com.semantive.alpha.mock

import com.semantive.geoys.dunno.pgSlickDriver.simple._
import com.semantive.geoys.tables._
import com.vividsolutions.jts.geom._
import scala.slick.session.Database.threadLocalSession
import scala.slick.jdbc.{StaticQuery => Q, GetResult}
import scala.Predef._

/**
 * Mock object, used only for tests.
 *
 * FixMe: database connection data - should not be stored in the code
 * FixMe: file paths should not be this specific (and platform-dependent)
 * FixMe: a lot of descriptions
 * FixMe: JTS
 * FixMe: IMPORTANT - logger
 * FixMe: IMPORTANT - handle incorrect geonames dumps
 * FixMe: filters: recursive, not chained
 *
 * ToDo:
 */
object MockDbSpammer
{
  private implicit val dbSession = Database.forURL("jdbc:postgresql://127.0.0.1/geoys", user = "geoys", password = "geoys", driver = "org.postgresql.Driver")

  private val dumpDirectory = "C:\\Geonames\\";

  /**
   * Filters out non-data (e.g. comments) from the geonames data source file.
   * @param line line from the raw source file from geonames dump
   * @return false, if line is a comment
   */
  private def sourceFileLineFilter(line: String): Boolean = {
    ! (line(0) == '#' || line(0) == ' ')
  }

  /**
   * FixMe: description
   * @param line
   * @return
   */
  private def sourceAllCountriesLineFilter(line: String): Boolean = {
    if(! sourceFileLineFilter(line))
      false
    else
      line.split("\t")(16) != "" // FixMe: what if the array is shorter
  }

  /**
   * FixMe: description
   * @param columnNo  index of filtered column
   * @param columnVal value to be matched
   * @return true, if
   */
  private def sourceColumnPrefixFilter(columnNo: Int, columnVal: String): (String => Boolean) = {
    (line: String) => line.split("\t")(columnNo).length >= columnVal.length && line.split("\t")(columnNo).substring(0, columnVal.length) == columnVal
  }

  // <editor-fold desc="Continent import">

  /**
   * Inserts continents' data into the database.
   *  Since the continents are rather not changing anytime soon, their data is hardcoded.
   */
  def insertContinents(): Unit = {
    dbSession withSession {
      Continent.forInsert.insert("AF", "Africa", "Africa", 6255146)
      Continent.forInsert.insert("AS", "Asia", "Asia", 6255147)
      Continent.forInsert.insert("EU", "Europe", "Europe", 6255148)
      Continent.forInsert.insert("NA", "North America", "North America", 6255149)
      Continent.forInsert.insert("SA", "South America", "South America", 6255150)
      Continent.forInsert.insert("OC", "Oceania", "Oceania", 6255151)
      Continent.forInsert.insert("AN", "Antarctica", "Antarctica", 6255152)
    }
  }

  // </editor-fold>

  // <editor-fold desc="Country import">

  /**
   * Inserts countries' data into the database.
   *  Uses countryInfo.txt and allCountries.txt from the geonames dump.
   */
  def importCountries(): Unit = {
    // 1: countryInfo
    val countriesRawFile = io.Source.fromFile(dumpDirectory + "countryInfo.txt")
    countriesRawFile.getLines().filter(sourceFileLineFilter).filter(line => line.split("\t")(16).length > 0).foreach(insertBasicCountryLine)
    countriesRawFile.close()

    // 2: allCountries
    val allCountriesRawFile = io.Source.fromFile(dumpDirectory + "allCountries.txt")
    allCountriesRawFile.getLines().filter(sourceAllCountriesLineFilter).filter(sourceColumnPrefixFilter(7, "PCL")).foreach(insertDetailedCountryLine)
    allCountriesRawFile.close()
  }

  /**
   * 1st step: inserts to the db data from countryInfo file.
   * @param lineStr prefiltered line from the source file
   */
  private def insertBasicCountryLine(lineStr: String): Unit = {
    def line = lineStr.split("\t")

    dbSession withSession {
      def continentQuery = for { c <- Continent if c.code === line(8) } yield c.id
      def continentId = continentQuery.first

      Country.forInsert.insert(line(0), line(1), line(3), line(4), line(4), None, continentId, line(7).toLong,line(16).toInt)
    }
  }

  /**
   * 2nd step: inserts detailed data (e.g. location) from the allCountries file.
   * @param line prefiltered line from the source file
   */
  private def insertDetailedCountryLine(line: String): Unit = {
    dbSession withSession {
      val q = for { c <- Country if c.geoId === line.split("\t")(0).toInt } yield c.location
      q.update(new Point(new Coordinate(line.split("\t")(5).toFloat, line.split("\t")(4).toFloat), new PrecisionModel(), 4326))
    }
  }

  // </editor-fold>

  // <editor-fold desc="Timezone import">

  def importTimezones(): Unit = {
    val timezonesRawFile = io.Source.fromFile(dumpDirectory + "timeZones.txt")
    timezonesRawFile.getLines().filter(sourceFileLineFilter).foreach(insertTimezoneLine)
    timezonesRawFile.close()
  }

  private def insertTimezoneLine(lineStr: String): Unit = {
    val line = lineStr.split("\t")

    dbSession withSession {

      def countryQuery = for { c <- Country if c.iso2 === line(0) } yield c.id
      def countryId = countryQuery.first

      Timezone.forInsert.insert(countryId, line(1), line(2).toFloat, line(3).toFloat, line(4).toFloat)
    }
  }

  // </editor-fold>

  // <editor-fold desc="ADM1 import">

  def importAdm1(): Unit = {
    // 1: admin1Codes
    val timezonesRawFile = io.Source.fromFile(dumpDirectory + "admin1CodesASCII.txt")
    timezonesRawFile.getLines().filter(sourceFileLineFilter).foreach(insertBasicAdm1Line)
    timezonesRawFile.close()

    // 2: allCountries
    val allCountriesRawFile = io.Source.fromFile(dumpDirectory + "allCountries.txt")
    allCountriesRawFile.getLines().filter(sourceAllCountriesLineFilter).filter(sourceColumnPrefixFilter(7, "ADM1")).foreach(insertDetailedAdm1Line)
    allCountriesRawFile.close()
  }

  private def insertBasicAdm1Line(lineStr: String): Unit = {
    def line = lineStr.split("\t")

    dbSession withSession {

      def countryQuery = for { c <- Country if c.iso2 === line(0).split("\\.")(0) } yield c.id
      def countryId = countryQuery.first

      ADM1.forInsert.insert(line(1), line(2), None, countryId, line(0).split("\\.")(1), None, None, line(3).toInt)
    }
  }

  /**
   * 2nd step: inserts detailed data (e.g. location) from the allCountries file.
   * @param lineStr prefiltered line from the source file
   */
  private def insertDetailedAdm1Line(lineStr: String): Unit = {
    def line = lineStr.split("\t")

    dbSession withSession {
      def timezoneQuery = for { tz <- Timezone if tz.name === line(17).toString } yield tz.id
      def timezoneId = timezoneQuery.firstOption

      val q = for { a <- ADM1 if a.geoId === line(0).toInt } yield (a.location ~ a.population ~ a.timezoneId.?)
      q.update((new Point(new Coordinate(line(5).toFloat, line(4).toFloat), new PrecisionModel(), 4326), line(14).toLong, timezoneId))
    }
  }

  // </editor-fold>

  // <editor-fold desc="ADM2 import">

  def importAdm2(): Unit = {
    // 1: admin2Codes
    val timezonesRawFile = io.Source.fromFile(dumpDirectory + "admin2Codes.txt")
    timezonesRawFile.getLines().filter(sourceFileLineFilter).foreach(insertBasicAdm2Line)
    timezonesRawFile.close()

    // 2: allCountries
    val allCountriesRawFile = io.Source.fromFile(dumpDirectory + "PL.txt")
    allCountriesRawFile.getLines().filter(sourceAllCountriesLineFilter).filter(sourceColumnPrefixFilter(7, "ADM2")).foreach(insertDetailedAdm2Line)
    allCountriesRawFile.close()
  }

  private def insertBasicAdm2Line(lineStr: String): Unit = {
    def line = lineStr.split("\t")

    dbSession withSession {

      def codes = line(0).split("\\.")

      def countryQuery = for { c <- Country if c.iso2 === codes(0) } yield c.id
      def countryId = countryQuery.first

      def adm1Query = for { a <- ADM1 if a.admCode === codes(1) } yield a.id
      def adm1Id = adm1Query.first

      ADM2.forInsert.insert(line(1), line(2), None, countryId, codes(2), adm1Id, None, None, line(3).toInt)
    }
  }

  /**
   * 2nd step: inserts detailed data (e.g. location) from the allCountries file.
   * @param lineStr prefiltered line from the source file
   */
  private def insertDetailedAdm2Line(lineStr: String): Unit = {
    def line = lineStr.split("\t")

    dbSession withSession {
      def timezoneQuery = for { tz <- Timezone if tz.name === line(17).toString } yield tz.id
      def timezoneId = timezoneQuery.firstOption

      val q = for { a <- ADM2 if a.geoId === line(0).toInt } yield (a.location ~ a.population ~ a.timezoneId.?)
      q.update((new Point(new Coordinate(line(5).toFloat, line(4).toFloat), new PrecisionModel(), 4326), line(14).toLong, timezoneId))
    }
  }

  // </editor-fold>

  // <editor-fold desc="ADM3 import">

  def importAdm3(): Unit = {
    val allCountriesRawFile = io.Source.fromFile(dumpDirectory + "PL.txt")
    allCountriesRawFile.getLines().filter(sourceAllCountriesLineFilter).filter(sourceColumnPrefixFilter(7, "ADM3")).foreach(insertAdm3Line)
    allCountriesRawFile.close()
  }

  private def insertAdm3Line(lineStr: String): Unit = {
    def line = lineStr.split("\t")
    println(lineStr)

    dbSession withSession {

      def countryQuery = for { c <- Country if c.iso2 === line(8) } yield c.id
      def countryId = countryQuery.first

      def adm1Query = for { a <- ADM1 if a.admCode === line(10) } yield a.id
      def adm1Id = adm1Query.first

      def adm2Query = for { a <- ADM2 if a.admCode === line(11) } yield a.id
      def adm2Id = adm2Query.first

      def timezoneQuery = for { tz <- Timezone if tz.name === line(17) } yield tz.id
      def timezoneId = timezoneQuery.firstOption

      ADM3.forInsert.insert(line(1), line(2), new Point(new Coordinate(line(5).toFloat, line(4).toFloat), new PrecisionModel(), 4326),
        countryId, line(12), adm1Id, adm2Id, line(14).toLong, timezoneId, line(0).toInt)
    }
  }

  // </editor-fold>

  // <editor-fold desc="ADM4 import">

  def importAdm4(): Unit = {
    val allCountriesRawFile = io.Source.fromFile(dumpDirectory + "PL.txt")
    allCountriesRawFile.getLines().filter(sourceAllCountriesLineFilter).filter(sourceColumnPrefixFilter(7, "ADM4")).foreach(insertAdm4Line)
    allCountriesRawFile.close()
  }

  private def insertAdm4Line(lineStr: String): Unit = {
    def line = lineStr.split("\t")
    println(lineStr)

    dbSession withSession {

      def countryQuery = for { c <- Country if c.iso2 === line(8) } yield c.id
      def countryId = countryQuery.first

      def adm1Query = for { a <- ADM1 if a.admCode === line(10) } yield a.id
      def adm1Id = adm1Query.first

      def adm2Query = for { a <- ADM2 if a.admCode === line(11) } yield a.id
      def adm2Id = adm2Query.first

      def adm3Query = for { a <- ADM3 if a.admCode === line(12) } yield a.id
      def adm3Id = adm3Query.first

      def timezoneQuery = for { tz <- Timezone if tz.name === line(17) } yield tz.id
      def timezoneId = timezoneQuery.firstOption

      ADM4.forInsert.insert(line(1), line(2), new Point(new Coordinate(line(5).toFloat, line(4).toFloat), new PrecisionModel(), 4326),
        countryId, line(12), adm1Id, adm2Id, adm3Id, line(14).toLong, timezoneId, line(0).toInt)
    }
  }

  // </editor-fold>

  def main(arguments: Array[String]): Unit =
  {
//    insertContinents()
//    importCountries()
//    importTimezones()
//    importAdm1()
//    importAdm2()
//    importAdm3()
//    importAdm4()

  }
}