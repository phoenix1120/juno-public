package controllers

import play.api.mvc.{Controller, Action}

object SlippyMap extends Controller {

  def getMap = Action{
    Ok(views.html.slippymap())
  }
}
