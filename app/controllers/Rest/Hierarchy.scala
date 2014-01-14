package controllers.Rest

import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current

import dao._
import utils.JsonTemplates

/**
 * REST service controller for hierarchical services (i.e. services basing on parent-child relations between PPLs).
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Hierarchy extends Controller {

  /**
   * Children service.
   *  Returns list of children of given feature.
   *
   * @param geonameId id of the requested feature
   * @param lang      preferred lang of names of the returned features
   * @return          JSON
   */
  def children(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val features = Features.getChildren(geonameId, lang)

    if(features.length == 0)
      NotFound
    else
      Ok(JsonTemplates.childrenToJson(features))
  }

  /**
   * Hierarchy service.
   *  Returns list of features higher in the hierarchy.
   *
   * @param geonameId id of the requested feature
   * @param lang      preferred lang of names of the returned features
   * @return          JSON
   */
  def hierarchy(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val features = Features.getHierarchy(geonameId, lang)

    if(features.length == 0)
      NotFound
    else
      Ok(JsonTemplates.hierarchyToJson(features))
  }

  /**
   * Siblings service.
   *  Returns list of siblings (i.e. features with the same parentId) of given feature.
   *
   * @param geonameId id of the requested feature
   * @param lang      preferred lang of names of the returned features
   * @return          JSON
   */
  def siblings(geonameId: Int, lang: String) = DBAction { implicit rs =>

    val features = Features.getSiblings(geonameId, lang)

    if(features.length == 0)
      NotFound
    else
      Ok(JsonTemplates.siblingsToJson(features))
  }
}
