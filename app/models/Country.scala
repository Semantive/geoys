package models

import com.vividsolutions.jts.geom.Point

case class Country(id: Option[Long], name: String, asciiName: String, iso2: String, iso3: String, fips: String,
                   location: Option[Point], continentId: Long, population: Long, geoId: Long)

