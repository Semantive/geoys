package models

import com.vividsolutions.jts.geom.Point

case class PopulatedPlace(id: Option[Long], name: String, asciiName: String, code: String, timezoneId: Option[Long],
                          location: Option[Point], countryId: Long, adm1Id: Option[Long], adm2Id: Option[Long],
                          adm3Id: Option[Long], adm4Id: Option[Long], parentId: Option[Long], population: Option[Long],
                          geoId: Long)