package models

private[models] trait DAO extends ContinentComponent
  with CountryComponent
  with TimezoneComponent {

  val Continents = new Continents
  val Countries = new Countries
  val Timezones = new Timezones
}