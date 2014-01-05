package models

/**
 * Country data representation (in terms of countryInfo service, not country as a location nor a feature).
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 *
 * @param geonameId       geonameId (can be used to find location of the country from features table).
 * @param iso2Code        two-letter ISO code of the country.
 * @param iso3Code        three-letter ISO code of the country.
 * @param isoNumeric      numeric ISO code of the country.
 * @param population      fips code of the country.
 * @param continentId     id of the continent the country is located in.
 * @param topLevelDomain  tld given to the country.
 * @param currencyCode    currency used in the country.
 */
case class Country(
  geonameId:        Int,
  iso2Code:         String,
  iso3Code:         String,
  isoNumeric:       String,
  fipsCode:         String,
  population:       Long,
  continentId:      Int,
  topLevelDomain:   String,
  currencyCode:     String
  // fixMe: languages representation
  // fixMe: neighbours representation
) extends AbstractEntity




