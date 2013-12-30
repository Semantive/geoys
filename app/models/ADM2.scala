package models

import com.vividsolutions.jts.geom.Point

case class ADM2(id: Option[Long], name: String, asciiName: String, admCode: String, timezoneId: Option[Long],
                location: Option[Point], countryId: Long, adm1Id: Long, population: Option[Long], geoId: Long)
