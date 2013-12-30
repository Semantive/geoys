package models

private[models] trait DAO extends ContinentComponent
  with CountryComponent
  with TimezoneComponent
  with ADM1Component
  with ADM2Component
  with ADM3Component
  with ADM4Component {

  val Continents = new Continents
  val Countries = new Countries
  val Timezones = new Timezones
  val ADM1s = new ADM1s
  val ADM2s = new ADM2s
  val ADM3s = new ADM3s
  val ADM4s = new ADM4s
}