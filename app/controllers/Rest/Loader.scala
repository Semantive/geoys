package controllers.Rest

import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current

import loader.{Loader => GeonamesLoader}

object Loader extends Controller {

  def loader() = DBAction {implicit rs =>
    try {
      new GeonamesLoader().loadData()
    } catch {
      case e: java.sql.BatchUpdateException => {
        if (e.getNextException != null) {
          throw e.getNextException
        } else {
          throw e
        }
      }
    }
    Ok("Loader finished.")
  }


}
