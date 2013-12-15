package com.semantive.geoys.tables

import com.vividsolutions.jts.geom.Point

/**
 * Populated place (PPL).
 *
 * Levels:
 *  0     - PPL, PPLC, PPLA, etc
 *  1..4  - PPLA1..PPLA4
 *  5     - PPLX
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object PopulatedPlace extends FeatureTable[(Option[Int], String, String, Point, Int, Option[Int], Option[Int], Option[Int], Option[Int], Option[Int], Int, Long, Int)]("PPL") {
  /** Id of the country ADM belongs to. */
  def countryId   = column[Int]("country_id")
  /** Id of the parent ADM1. */
  def adm1Id      = column[Int]("adm1_id")
  /** Id of the parent ADM2. */
  def adm2Id      = column[Int]("adm2_id")
  /** Id of the parent ADM3. */
  def adm3Id      = column[Int]("adm3_id")
  /** Id of the parent ADM4. */
  def adm4Id      = column[Int]("adm4_id")
  /** Id of the parent PPL. */
  def parentId    = column[Int]("parent_id")
  /** Level of the PPL. */
  def level       = column[Int]("level")

  /** Default projection for SELECT * queries. */
  def * = id.? ~ name ~ asciiName ~ location ~ countryId ~ adm1Id.? ~ adm2Id.? ~ adm3Id.? ~ adm4Id.? ~ parentId.? ~ level ~ population ~ geoId
  /** Projection for INSERT queries. */
  def forInsert = name ~ asciiName ~ location ~ countryId ~ adm1Id.? ~ adm2Id.? ~ adm3Id.? ~ adm4Id.? ~ parentId.? ~ level ~ population ~ geoId

  /** REFERENCES key on country.id. */
  def fkPplCountry = foreignKey("fk_" + tableName +  "_country", countryId, Country)(_.id)
  /** REFERENCES key on adm1.id. */
  def fkPplAdm1 = foreignKey("fk_" + tableName +  "_adm1", adm1Id, ADM1)(_.id)
  /** REFERENCES key on adm2.id. */
  def fkPplAdm2 = foreignKey("fk_" + tableName +  "_adm2", adm2Id, ADM2)(_.id)
  /** REFERENCES key on adm3.id. */
  def fkPplAdm3 = foreignKey("fk_" + tableName +  "_adm3", adm3Id, ADM3)(_.id)
  /** REFERENCES key on adm4.id. */
  def fkPplAdm4 = foreignKey("fk_" + tableName +  "_adm4", adm4Id, ADM4)(_.id)
  /** REFERENCES key on PPL.id. */
  def kfPplPpl  = foreignKey("fk_" + tableName + "_ppl", parentId, PopulatedPlace)(_.id)
}