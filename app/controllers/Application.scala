package controllers

import play.api._
import play.api.mvc._
import slick.backend.DatabaseConfig
import slick.driver.H2Driver

class Application(dbConfig: DatabaseConfig[H2Driver]) extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
