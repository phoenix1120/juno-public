package controllers

import play.api.mvc.{Controller, Action}
import junoshared.web.WebRequest
import play.api.libs.json.{Json, JsValue}
import play.api.libs.json.Json._
import scala.math._


object SlippyMap extends Controller {

  val tileCacheBaseUrl = "http://localhost:9001/tilecache/"

  /**
   * Main page handler for slippy map
   * @return
   */
  def getMap = Action{

    try{
      val hwGetRes = getHelloWorldFromTileCache
      val hwPostRes = getHelloWorldPostFromTileCache

      Ok(views.html.slippymap(hwGetRes, hwPostRes))
    }
    catch{
      case e: Exception =>
        println("Exception: " + e.getMessage)
        Ok(views.html.error("Exception: " + e.getMessage))
    }
  }

  /**
   * Call the HelloWorld GET api on tile cache
   * @return
   */
  def getHelloWorldFromTileCache: String = {

    val getHwUrl = tileCacheBaseUrl + "helloworld"

    WebRequest.get(getHwUrl)
  }

  /**
   * Call the HelloWorld POST api on tile cache
   * @return
   */
  def getHelloWorldPostFromTileCache: String = {
    val postHwUrl = tileCacheBaseUrl + "helloworldPost"

    val postJson = Map("helloWorld" -> "Hello? Is anyone out there?")
    WebRequest.postJson(postHwUrl, Json.toJson(postJson))
  }
}
