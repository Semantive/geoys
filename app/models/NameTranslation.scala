package models

/**
 * Name of the given geoname feature.
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 *
 * @param geonameId id of the feature.
 * @param language  language of the translation.
 * @param name      translated name of the feature.
 */
case class NameTranslation (
  id:         Option[Int],
  geonameId:  Int,
  language:   String,
  name:       String
) extends AbstractEntity
