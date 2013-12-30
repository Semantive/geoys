package models

import com.vividsolutions.jts.geom.Point

case class ADM1(id: Option[Long], name: String, asciiName: String, admCode: String, timezoneId: Option[Long],
                location: Option[Point], countryId: Long, population: Option[Long], geoId: Long)
