package models

import com.vividsolutions.jts.geom.Point

/**
 * Geonames feature representation.
 *  Geonames feature means a single entity (row) from allCountries file. It is idetified by geonameId and has
 * location and can be placed in features' hierarchy.
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 *
 * @param geonameId     id from the Geoname's data dump.
 * @param featureClass  feature class (general type of the feature).
 * @param featureCode   feature code (detailed type of the feature).
 * @param admCode       adm code of the feature (for A features only).
 * @param timezoneId    id of the timezone feature is placed in.
 * @param location      location (JTS Point) of the feature.
 * @param countryId     id of the country feature belongs to (only for non-country and non-continent features).
 * @param population    population of the feature.
 * @param adm1Id        id if the 1st ADM level division of the feature (only for ADM2 & lower).
 * @param adm2Id        id if the 2nd ADM level division of the feature (only for ADM3 & lower).
 * @param adm3Id        id if the 3rd ADM level division of the feature (only for ADM4 & lower).
 * @param adm4Id        id if the 4th ADM level division of the feature (only for lower than ADM4).
 * @param parentId      id of the feature that stands one step higher than this feature (mostly for P features).
 */
case class Feature(
  geonameId:      Int,
  featureClass:   String,
  featureCode:    String,
  admCode:        Option[String],
  countryId:      Option[Int],
  adm1Id:         Option[Int],
  adm2Id:         Option[Int],
  adm3Id:         Option[Int],
  adm4Id:         Option[Int],
  parentId:       Option[Int],
  timezoneId:     Option[Int],
  location:       Option[Point],
  population:     Option[Long]
) extends AbstractEntity
