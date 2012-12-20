package controllers

import play.api.mvc.{AnyContent, Action, Controller}
import play.api.Play
import akka.event.slf4j.Logger
import java.io._
import org.apache.commons.httpclient.{HttpStatus, HttpException, HttpClient}
import org.apache.commons.httpclient.methods.{GetMethod}

object TileCache extends Controller{

  /**
   * Hanldes the request for a tile cache tile
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

      val hc = new HttpClient()
      val hcget = new GetMethod(mapServerUri)

      var httpErr = ""
      var responseImg: InputStream = null

      try {
        val statusCode = hc.executeMethod(hcget)

        if (statusCode != HttpStatus.SC_OK){
          httpErr = "MapServer GET failed with status code " + statusCode.toString
          Logger("application").warn(httpErr)
        }
        else{
          responseImg = hcget.getResponseBodyAsStream

          // copy to local file
          val newOutStream = new FileOutputStream(new File(play.Play.application().path() + "/public/images/tiles/tile1.png"))
          copyStream(responseImg, newOutStream)
          newOutStream.flush()
        }
      }
      catch{
        case httpex: HttpException => {
          httpErr = "HttpException thrown attempting MapServer GET on " + mapServerUri + ": " + httpex.getMessage
          Logger("application").warn(httpErr)
        }
        case ioex: IOException => {
          httpErr = "IOExcpetion thrown attempting MapServer GET on " + mapServerUri + ": " + ioex.getMessage
          Logger("application").warn(httpErr)
        }
      }
      finally{
        hcget.releaseConnection()
      }

    }
    catch {
      case e: Exception =>
        Logger("error").error("Exception thrown handling getTileImg: " + e.getMessage)
    }

    controllers.Assets.at(path, file)
  }

  /**
   * Consumes the given InputStream and byte array chunks to the provided
   * closure. The first argument to the closure will be the array of bytes
   * (perhaps partially filled.) The second argument will be the count of the
   * number of bytes that were read.
   *
   * Note: call with the curried style. The stream is closed when finished.
   */
  def forEachBytes(in: InputStream)(closure: (Array[Byte], Int) => Unit) {
    val iobuf = new Array[Byte](1024)
    var done = false

    while ( ! done ) {
      val bytes = in.read(iobuf)
      if ( bytes > 0 ) {
        closure(iobuf,bytes)
      } else {
        done = true
      }
    }

    in.close()
  }


  /**
   * Utility method for copying an InputStream to an OutputStream. Both
   * streams are closed when finished.
   *
   * All the streams are closed when finished.
   */
  def copyStream(in: InputStream, out: OutputStream) {
    var childEx: Exception = null

    try{
      forEachBytes(in) { (bytes, count) =>
        out.write(bytes,0,count)
      }
    }
    catch {
      case e: Exception =>
        childEx = e
    }
    finally {
      if (in != null) in.close()
      if (out != null) out.close()

      if (childEx != null) throw childEx
    }
  }
}

