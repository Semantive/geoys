package models

import com.vividsolutions.jts.geom.Point

case class AdministrativeUnit(id: Option[Long],
                              name: String,
                              asciiName: String,
                              admCode: String,
                              timezoneId: Option[Long],
                              location: Option[Point],
                              countryId: Long,
                              population: Option[Long],
                              geoId: Long,
                              adm1Id: Option[Long] = None,
                              adm2Id: Option[Long] = None,
                              adm3Id: Option[Long] = None,
                              level: Int = 1) extends AbstractEntity



