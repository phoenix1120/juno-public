package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import play.api.Play
import akka.event.slf4j.Logger

object TileCache extends Controller{

  def getTileImg(path: String, file: String): Action[AnyContent] = {

    try {
      Logger("application").debug("tile requested")

      // this controller method needs to take parameters by which you can look up the tile
      // if the tile isn't in the cache, ask the map server for it
      // once you have the actual png file, copy it to images/tiles/.../whatever
      // then send that back here

    }
    catch {
      case e: Exception =>
        Logger("error").error("Exception thrown handling getTileImg: " + e.getMessage)
    }

    controllers.Assets.at(path, file)
  }
}

