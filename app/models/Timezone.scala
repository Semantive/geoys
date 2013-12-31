package models

case class Timezone(id: Option[Long],
                    countryId: Long,
                    name: String,
                    gmtOffset: Double,
                    dstOffset: Double,
                    rawOffset: Double) extends AbstractEntity

