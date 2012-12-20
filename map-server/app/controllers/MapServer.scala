package controllers

import play.api.mvc.{Controller, AnyContent, Action}
import akka.event.slf4j.Logger


object MapServer extends Controller{

  /**
   * Handles the request for a map server tile
   * @param path
   * @param file
   * @return
   */
  def getTileImg(path: String, file: String): Action[AnyContent] = {

    try {
      Logger("application").debug("tile requested")

      // this controller method needs to take parameters by which it can generate a
      // new tile image

    }
    catch {
      case e: Exception =>
        Logger("error").error("Exception thrown handling getTileImg: " + e.getMessage)
    }

    controllers.Assets.at(path, file)
  }
}
