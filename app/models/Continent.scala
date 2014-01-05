package models

/**
 * Continent representation.
 *
 * @author Amadeusz Kosik <>
 *
 * @param geonameId   id of the continet in the Geonames' dump.
 * @param code        two-letter code of the continent.
 */
case class Continent(
  geonameId: Int,
  code: String
) extends AbstractEntity



