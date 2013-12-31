package models

case class Continent(id: Option[Long],
                     name: String,
                     asciiName: String,
                     code: String,
                     geoId: Long) extends AbstractEntity



