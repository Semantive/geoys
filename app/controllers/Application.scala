package controllers

import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current


object Application extends Controller {

  def index = DBAction {
    implicit rs =>
      Ok(views.html.index("Your new application is ready."))
  }

}