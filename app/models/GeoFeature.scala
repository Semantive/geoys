package models

import com.vividsolutions.jts.geom.Point

case class GeoFeature(id: Option[Long],
                      name: String,
                      asciiName: String,
                      fClass: String,
                      fCode: String,
                      admCode: Option[String],
                      timezoneId: Option[Long],
                      location: Option[Point],
                      countryId: Long,
                      population: Option[Long],
                      geoId: Long,
                      adm1Id: Option[Long] = None,
                      adm2Id: Option[Long] = None,
                      adm3Id: Option[Long] = None,
                      adm4Id: Option[Long] = None,
                      parentId: Option[Long] = None,
                      level: Option[Int]) extends AbstractEntity



