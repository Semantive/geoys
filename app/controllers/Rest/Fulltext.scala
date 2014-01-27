package controllers.Rest

import play.api.mvc._

/**
 * REST service controller for fulltext search services.
 *
 * @author Amadeusz Kosik <akosik@semantive.com>
 */
object Fulltext extends Controller {

  /**
   * FulltextSearch service.
   *  Returns list of features matching given input.
   *
   * @todo heavily under construction - define rest of the required/optional parameters
   *
   * @param input     name of the feature to match
   * @param limit     max number of results
   * @param lang      preferred lang of names of the returned features
   * @return          JSON
   */
  def fulltextSearch(input: String, limit: Int, lang: String) = TODO
}