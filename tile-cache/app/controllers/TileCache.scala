package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import play.api.Play
import akka.event.slf4j.Logger
import java.io._
import org.apache.commons.httpclient.{HttpStatus, HttpException, HttpClient}
import org.apache.commons.httpclient.methods.{GetMethod}
import junoshared.web.WebRequest
import play.api.libs.json.{Json, JsValue}
import play.api.libs.json.Json._


object TileCache extends Controller{

  /**
   * REST API - hello world GET
   * @return
   */
  def getHelloWorld  = Action {
    println("Processing request for  getHelloWorld")
    Ok("HelloWorld")
  }

  /**
   * REST API - hello world POST
   * @return
   */
  def handleHWPost = Action(parse.json) { request =>

    println("Processing request for handleHWPost")

    val hwData = (request.body \ "helloWorld").asOpt[String]
    if (hwData == None){
      BadRequest("Missing required param 'helloWorld'")
    }
    else {
      try {
        val jsonResponse = Map("helloWorldResponse" -> hwData.get)

        Ok(toJson(jsonResponse))
      }
      catch {
        case e: Exception =>
          println("Exception: " + e.getMessage)
          InternalServerError(e.getMessage)
      }
    }
  }

  /**
   * Handles the request for a tile cache tile
   * @param path
   * @param file
   * @return
   */
  def getTileImg(path: String, file: String): Action[AnyContent] = {

    try {
      Logger("application").debug("tile requested")

      // this controller method needs to take parameters by which you can look up the tile
      // if the tile isn't in the cache, ask the map server for it
      // once you have the actual png file, copy it to images/tiles/.../whatever
      // then send that back here

      val mapServerUri = "http://localhost:9002/assets/images/tiles/tile1.png"

      val responseImg = WebRequest.getResponseAsByteArray(mapServerUri)

      // copy to local file
      val newOutStream = new FileOutputStream(new File(play.Play.application().path() + "/public/images/tiles/tile1.png"))
      newOutStream.write(responseImg)

      newOutStream.flush()
    }
    catch {
      case e: Exception =>
        Logger("error").error("Exception thrown handling getTileImg: " + e.getMessage)
    }

    controllers.Assets.at(path, file)
  }


}

