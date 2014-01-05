package models

/**
 * Timezone data representation.
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 *
 * @param id          identifier (generated).
 * @param countryId   id of the country of the timezone.
 * @param name        name of the timezone (like Europe/Warsaw).
 * @param gmtOffset   GMT offset.
 * @param dstOffset   DST offset.
 * @param rawOffset   raw offset.
 */
case class Timezone(
  id: Option[Long],
  countryId: Long,
  name: String,
  gmtOffset: Double,
  dstOffset: Double,
  rawOffset: Double
) extends AbstractEntity

