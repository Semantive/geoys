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
  geonameId:    Int,
  language:     String,
  name:         String,
  isOfficial:   Boolean
) extends AbstractEntity
