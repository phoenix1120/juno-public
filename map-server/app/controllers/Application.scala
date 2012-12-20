package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def test = Action {
    Ok(views.html.main("test"))
  }
  
}