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
  id:  Option[Int],
  countryId: Int,
  name: String,
  gmtOffset: Double,
  dstOffset: Double,
  rawOffset: Double
) extends AbstractEntity {

  def this(countryId: Int, name: String, gmtOffset: Double, dstOffset: Double, rawOffset: Double) =
    this(None, countryId, name, gmtOffset, dstOffset, rawOffset)





}

