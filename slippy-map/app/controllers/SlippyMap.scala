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

    val postJson = Map("helloWorld" -> "this is my hello world value")
    WebRequest.postJson(postHwUrl, Json.toJson(postJson))
  }

  /*/*
  * This is the code that calculates the tiles needed
  * */
  case class Tile(x: Int,y: Int, z: Short){
    def toLatLon = new LatLonPoint(
      toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1<<z))))),
      x.toDouble / (1<<z) * 360.0 - 180.0,
      z)
    def toURI = new java.net.URI("http://tile.openstreetmap.org/"+z+"/"+x+"/"+y+".png")
  }

  case class LatLonPoint(lat: Double, lon: Double, z: Short){
    def toTile = new Tile(
      ((lon + 180.0) / 360.0 * (1<<z)).toInt,
      ((1 - log(tan(toRadians(lat)) + 1 / cos(toRadians(lat))) / Pi) / 2.0 * (1<<z)).toInt,
      z)
  }

  //Usage:
  val point = LatLonPoint(51.51202,0.02435,17)
  val tile = point.toTile
  // ==> Tile(65544,43582,17)
  val uri = tile.toURI
  // ==> http://tile.openstreetmap.org/17/65544/43582.png
*/

}
