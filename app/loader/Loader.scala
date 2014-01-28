package loader

import utils.{CacheManager, Database}
import dao._

import scala.slick.jdbc.{StaticQuery => Q}
import scala.slick.session.Database.threadLocalSession
import utils.pgSlickDriver.simple._
import scala.io.Source
import play.Logger
import models.Country
import models.NameTranslation
import scala.Some
import models.Feature
import models.Timezone
import models.Continent
import com.vividsolutions.jts.geom.{Coordinate, PrecisionModel, GeometryFactory}
import org.apache.commons.lang.time.DurationFormatUtils
import play.api.cache.EhCachePlugin
import play.api.Play.current


/**
 * @author Piotr Jędruszuk <pjedruszuk@semantive.com>
 */
class Loader {

  val database = Database.createConnection()
  val geometryFactory = new GeometryFactory(new PrecisionModel(), 4326)

  def loadData() = {
    val startTimestamp = System.currentTimeMillis
    loadContinents()
    loadCountries()
    loadTimezones()
    loadAllCountries()
    processHierarchy()
    loadWikipediaLinks()


    Logger.info("Clearing cache, it's not needed anymore...")
    for(p <- current.plugin[EhCachePlugin]){
      p.manager.clearAll()
    }

    loadTranslations()

    Logger.debug("Cleaning up...")
    System.gc()

    val endTimestamp = System.currentTimeMillis
    Logger.info("Loading completed. Total time: " + DurationFormatUtils.formatDurationHMS(endTimestamp - startTimestamp))
  }

  private def loadContinents() = {
    Logger.info("Loading countries...")
    val continents = List(
      new Continent(6255146, "AF"),
      new Continent(6255147, "AS"),
      new Continent(6255148, "EU"),
      new Continent(6255149, "NA"),
      new Continent(6255150, "SA"),
      new Continent(6255151, "OC"),
      new Continent(6255152, "AN")
    )

    val lang = "EN_US"
    val translations = List(
      new NameTranslation(6255146, lang, "Africa", true),
      new NameTranslation(6255147, lang, "Asia", true),
      new NameTranslation(6255148, lang, "Europe", true),
      new NameTranslation(6255149, lang, "North America", true),
      new NameTranslation(6255150, lang, "South America", true),
      new NameTranslation(6255151, lang, "Oceania", true),
      new NameTranslation(6255152, lang, "Antarctica", true)
    )

    database withTransaction {
      Continents insertAll (continents: _*)
      Logger.info("Loaded continents: " + continents.size)
      NameTranslations insertAll (translations: _*)
      this.continents ++ continents.map(v => (v.code, v)).toMap
    }
  }

  private def loadCountries() = {

    def parseLine(line: String) = {
      val splittedLine = line.split("\t")

      def createCountry: Country = {
        new Country(
          geonameId = splittedLine(16).toInt,
          iso2Code = splittedLine(0),
          iso3Code = splittedLine(1),
          isoNumeric = splittedLine(2),
          fipsCode = Option(splittedLine(3)),
          population = splittedLine(7).toLong,
          continentId = getContinent(splittedLine(8)).geonameId,
          topLevelDomain = splittedLine(9),
          currencyCode = splittedLine(10))
      }

      if (splittedLine(16) == null || splittedLine(16).isEmpty) {
        Logger.warn("Dropping country line because geonameId is empty: " + line)
        null
      } else {
        createCountry
      }
    }


    database withTransaction {
      val source = Source.fromFile("/Users/piotrjedruszuk/Gisgraphy/countryInfo.txt")
      val countries = source.getLines().filterNot(_.startsWith("#")).map(parseLine).filter(_ != null).toList

      Countries insertAll (countries: _*)
      Logger.info("Loaded countries: " + countries.size)
      this.countries ++ countries.map(v => (v.iso2Code, v)).toMap
    }

  }

  private def loadTimezones() = {
    def parseLine(line: String) = {
      val splittedLine = line.split("\t")

      new Timezone(
        id = null,
        countryId = getCountry(splittedLine(0)).geonameId,
        name = splittedLine(1),
        gmtOffset = splittedLine(2).toDouble,
        dstOffset = splittedLine(3).toDouble,
        rawOffset = splittedLine(4).toDouble
      )
    }

    database withTransaction {
      val source = Source.fromFile("/Users/piotrjedruszuk/Gisgraphy/timeZones.txt")
      val timeZones = source.getLines().drop(1).map(parseLine).toArray
      Timezones.insertion insertAll (timeZones: _*)
      Logger.info("Loaded timezones: " + timeZones.size)
    }
  }

  private def loadAllCountries() = {
    def parseAdmCodes1(line: String) = {
      val splittedLine = line.split("\t")
      val codes: Array[String] = splittedLine(0).split("\\.")

      val country = getCountry(codes(0))
      val feature = new Feature(
        splittedLine(3).toInt,
        "A", "ADM1",
        Option(codes(1)),
        Option(country.geonameId)
      )
      adm1s ++ (country.iso2Code + "." + feature.admCode.get, feature)
      features ++ (feature.geonameId.toString, feature)
      feature
    }

    def parseAdmCodes2(line: String) = {
      val splittedLine = line.split("\t")
      def codes = splittedLine(0).split("\\.")

      val country = getCountry(codes(0))
      val feature = new Feature(
        splittedLine(3).toInt,
        "A", "ADM2",
        Option(codes(2)),
        Option(country.geonameId),
        getAdministrativeDivision(1, codes(1), country) match {
          case Some(f) => Some(f.geonameId)
          case None => None
        }
      )

      adm2s ++ (country.iso2Code + "." + feature.admCode.get, feature)
      features ++ (feature.geonameId.toString, feature)
      feature
    }

    def updateExistingFeatures(line: String) : Feature = {
      val splittedLine = line.split("\t")

      val timezone = getTimezone(splittedLine(17))
      new Feature(
        splittedLine(0).toInt,
        "A", splittedLine(7),
        point(splittedLine(4).toDouble, splittedLine(5).toDouble),
        Option(splittedLine(14).toLong),
        Option(timezone) match {case Some(t) => t.id; case None => None }
      )
    }


    def insertNewAdministrativeDivisions(line: String) : Feature = {
      val splittedLine = line.split("\t")

      val country = getCountry(splittedLine(8))
      val timezone = getTimezone(splittedLine(17))

      if (country == null) {
        Logger.warn("Dropping ADM line because of unknown country: " + line)
        null
      } else {
        val feature = new Feature(
          splittedLine(0).toInt,
          splittedLine(6),
          splittedLine(7),
          if (splittedLine(7) == "ADM4") Option(splittedLine(13)) else Option(splittedLine(12)),
          Option(country.geonameId),
          getAdministrativeDivision(1, splittedLine(10), country) match {
            case Some(f) => Some(f.geonameId)
            case None => None
          },
          getAdministrativeDivision(2, splittedLine(11), country) match {
            case Some(f) => Some(f.geonameId)
            case None => None
          },
          if (splittedLine(7) == "ADM4") getAdministrativeDivision(3, splittedLine(12), country) match {
            case Some(f) => Some(f.geonameId)
            case None => None
          } else None,
          None,
          None,
          Option(timezone) match {case Some(t) => t.id; case None => None },
          Option(point(splittedLine(4).toDouble, splittedLine(5).toDouble)),
          Option(splittedLine(14).toLong),
          None
        )

        feature.featureCode match {
          case "ADM3" => adm3s ++ (country.iso2Code + "." + feature.admCode.get, feature)
          case "ADM4" => adm4s ++ (country.iso2Code + "." + feature.admCode.get, feature)
        }
        features ++ (feature.geonameId.toString, feature)

        feature
      }
    }

    def insertPopulatedPlace(line: String) : Feature = {
      val splittedLine = line.split("\t")
      val country: Country = getCountry(splittedLine(8))
      val timezone = getTimezone(splittedLine(17))

      val feature = new Feature(
        splittedLine(0).toInt,
        splittedLine(6),
        splittedLine(7),
        Option(splittedLine(12)),
        Option(country.geonameId),
        getAdministrativeDivision(1, splittedLine(10), country) match {
          case Some(f) => Some(f.geonameId)
          case None => None
        },
        getAdministrativeDivision(2, splittedLine(11), country) match {
          case Some(f) => Some(f.geonameId)
          case None => None
        },
        getAdministrativeDivision(3, splittedLine(12), country) match {
          case Some(f) => Some(f.geonameId)
          case None => None
        },
        getAdministrativeDivision(4, splittedLine(13), country) match {
          case Some(f) => Some(f.geonameId)
          case None => None
        },
        None,
        Option(timezone) match {case Some(t) => t.id; case None => None },
        Option(point(splittedLine(4).toDouble, splittedLine(5).toDouble)),
        Option(splittedLine(14).toLong),
        None
      )
      features ++ (feature.geonameId.toString, feature)
      feature
    }

    var rows = 0
    def selectParseFunction(line: String): Feature = {
      val splittedLine = line.split("\t")

      val administrativeDivision: Feature = splittedLine(7) match {
        case "ADM1" | "ADM2" => updateExistingFeatures(line)
        case "ADM3" | "ADM4" => insertNewAdministrativeDivisions(line)
        case _ => null
      }

      val populatedPlace: Feature = if (splittedLine(6) == "P") insertPopulatedPlace(line) else null

      if(rows % 100 ==0) print("\rProcessed rows: %d".format(rows))
      rows += 1

      Option(administrativeDivision).getOrElse(Option(populatedPlace).getOrElse(null))
    }

    database withTransaction {
      val adm1source = Source.fromFile("/Users/piotrjedruszuk/Gisgraphy/admin1CodesASCII.txt")
      val adm1s = adm1source.getLines().map(parseAdmCodes1).toArray
      Features.adm1insertion insertAll (adm1s: _*)
      Logger.info("Loaded ADM1s: " + adm1s.size)

      val adm2source = Source.fromFile("/Users/piotrjedruszuk/Gisgraphy/admin2Codes.txt")
      val adm2s = adm2source.getLines().map(parseAdmCodes2).toArray
      Features.adm2insertion insertAll (adm2s: _*)

      Logger.info("Loaded ADM2s: " + adm2s.size)
    }

    Logger.info("Processing allCountries file...")

    var iteration = 1
    def processData(filter: (String => Boolean)) {
      database withTransaction {
        rows = 0
        Logger.info("Iteration " + iteration)
        val allDataSource = Source.fromFile("/Users/piotrjedruszuk/Gisgraphy/allCountries.txt")

        val allData = allDataSource.getLines().filter(filter).map(selectParseFunction).filter(_ != null).toList

        Logger.info("Updating data...")
        val dataToUpdate = allData.filter(feature => feature.featureCode == "ADM1" || feature.featureCode == "ADM2")
        dataToUpdate.foreach {
          feature => {
            val query = for {f <- Features if f.geonameId === feature.geonameId} yield f.location.? ~ f.population.? ~ f.timezoneId.?
            query.update((feature.location, feature.population, feature.timezoneId))
          }
        }
        println()
        Logger.info("Updated administrative units: " + dataToUpdate.size)

        Logger.info("Inserting data...")
        val dataToInsert = allData.filter(feature => feature.featureCode == "ADM3" || feature.featureCode == "ADM4" || feature.featureClass == "P")
        Features.* insertAll (dataToInsert: _*)
        Logger.info("Inserted ADM3s, ADM4s, PPLs: " + dataToInsert.size)
      }
    }

    processData(_.split("\t")(7) == "ADM1")
    iteration += 1
    processData(_.split("\t")(7) == "ADM2")
    iteration += 1
    processData(_.split("\t")(7) == "ADM3")
    iteration += 1
    processData(_.split("\t")(7) == "ADM4")
    iteration += 1
    processData(line => {
      val split = line.split("\t")
      split.size > 6 && split(6) == "P"
    })
  }


  private def loadTranslations() = {
    def parseTranslation(line: String) = {
      val splittedLine = line.split("\t")

      NameTranslation(
        geonameId = splittedLine(1).toInt,
        language = splittedLine(2),
        name = splittedLine(3),
        isOfficial = splittedLine.size > 5 && splittedLine(5) == "1"
      )
    }

    Logger.info("Processing translations...")

    val translationsSource = Source.fromFile("/Users/piotrjedruszuk/Gisgraphy/alternateNames.txt")
    val translations = translationsSource.getLines().filter(_.split("\t")(2) != "link").map(parseTranslation).toList

    database withTransaction {
      NameTranslations.* insertAll(translations : _*)
      Logger.info("Translation entries processed: " + translations.size)
    }

  }

  private def loadWikipediaLinks() = {

    def parseLinks(line: String) = {
      val splittedLine = line.split("\t")
      (splittedLine(1).toInt, splittedLine(3))
    }

    val linksSource = Source.fromFile("/Users/piotrjedruszuk/Gisgraphy/alternateNames.txt")
    val links = linksSource.getLines().filter(_.split("\t")(2) == "link").map(parseLinks).toList

    Logger.info("Processing Wikipedia links...")
    var rows = 0
    database withTransaction {
      links foreach {
        pair =>
          if(rows % 100 ==0) print("\rProcessed rows: %d".format(rows))
          rows += 1

          if (getFeature(pair._1) != null) {
            val query = for {f <- Features if f.geonameId === pair._1} yield f.wikiLink.?
            query.update(Option(pair._2))
          }
      }
      Logger.info("Processed links: " + links.size)
    }
  }

  private def processHierarchy() = {
    Logger.info("Processing hierarchies...")

    def parseHierarchy(line: String) = {
      val splittedLine = line.split("\t")
      (splittedLine(0).toInt, splittedLine(1).toInt)
    }

    val hierarchySource = Source.fromFile("/Users/piotrjedruszuk/Gisgraphy/hierarchy.txt")
    val hierarchy = hierarchySource.getLines().map(parseHierarchy).toList

    var rows = 0
    database withTransaction {
      hierarchy.foreach {
        pair =>
          if(rows % 100 ==0) print("\rProcessed rows: %d".format(rows))
          rows += 1

          if (getFeature(pair._1) != null) {
            val query = for {f <- Features if f.geonameId === pair._2} yield  f.parentId.?
            query.update(Option(pair._1))
          }
      }
      Logger.info("Hierarchy entries processed: " + hierarchy.size)
    }
  }

  val continents = new CacheManager[Continent]("continent")
  val countries = new CacheManager[Country]("countries")
  val timeZones = new CacheManager[Timezone]("time_zones")
  val adm1s = new CacheManager[Feature]("adm1")
  val adm2s = new CacheManager[Feature]("adm2")
  val adm3s = new CacheManager[Feature]("adm3")
  val adm4s = new CacheManager[Feature]("adm4")
  val admCaches = Map((1, adm1s), (2, adm2s), (3, adm3s), (4, adm4s))
  val features = new CacheManager[Feature]("feature")

  private def getCountry(iso2Code: String) = countries.getOrElse(iso2Code, key => Countries.unique(Query(Countries).filter(_.iso2Code === key)).getOrElse(null))

  private def getContinent(code: String) = continents.getOrElse(code, key => Continents.unique(Query(Continents).filter(_.code === key)).getOrElse(null))

  private def getTimezone(name: String) = timeZones.getOrElse(name, key => Timezones.unique(Query(Timezones).filter(_.name === key)).getOrElse(null))

  private def getFeature(geonameId : Int) = features.getOrElse(geonameId.toString, key => Features.unique(Query(Features).filter(_.geonameId === geonameId)).getOrElse(null))

  private def getAdministrativeDivision(level: Int, code: String, country: Country): Option[Feature] = {
    def getAdministrativeDivision(country: Country, code: String, cache: CacheManager[Feature]): Option[Feature] = {
      val cacheCode: String = country.iso2Code + "." + code
      val option = Option(cache.get(cacheCode))
      if (option.isDefined) {
        if (option.get.geonameId == -1) None else option
      } else {
        try {
          val cacheOption = Features.unique(Query(Features).filter(_.admCode === code).filter(_.countryId === country.geonameId).filter(_.featureCode === "ADM" + level))
          if (cacheOption.isDefined) {
            cache ++(cacheCode, cacheOption.get)
            cacheOption
          } else {
            //fallback to level - 1, if possible
            val fallbackOption = Features.unique(Query(Features).filter(_.admCode === code).filter(_.countryId === country.geonameId).filter(_.featureCode === "ADM" + (level-1)))
            if (fallbackOption.isDefined) {
              cache ++(cacheCode, fallbackOption.get)
              fallbackOption
            } else {
              //looks like, that this record doesn't exist, possible Geonames data inconsistency
              cache ++(cacheCode, new Feature(-1))
              None
            }
          }
        } catch {
          case e: NonUniqueResult => {
            Logger.warn("Duplicates found for ADM Level " + level + ", code: " + code + ", country " + country.iso2Code + ". This will be resolved to null for this row")
            None
          }
        }

      }
    }

    if (code == null || code.isEmpty) None else getAdministrativeDivision(country, code, admCaches(level))
  }

  private def point(latitude : Double, longitude: Double)  = {
    geometryFactory.createPoint(new Coordinate(longitude, latitude))
  }
}
