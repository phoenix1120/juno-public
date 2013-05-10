package junoshared.web

import junoshared.system.environment.Environment
import org.apache.commons.httpclient._
import methods.{StringRequestEntity, PostMethod, GetMethod}
import org.apache.http.conn.params.ConnRoutePNames
import java.io.{File, InputStream}
import play.api.libs.json.JsValue
import org.apache.commons.httpclient.protocol.Protocol
import scala.Left
import scala.Right

object WebRequest {

  /**
   * Get from a url - returns the response body as a string
   * @param url
   * @return
   */
  def get(url: String): String = {
    get(url, true).left.get
  }

  /**
   * Get from a url - returns the response body as a byte array
   * @param url
   * @return
   */
  def getResponseAsByteArray(url: String): Array[Byte] = {
    get(url, false).right.get
  }

  /**
   * Get from a url - returns response body as either a string or byte array depending on param passed in
   * @param url
   * @param responseAsString
   * @return
   */
  private def get(url: String, responseAsString: Boolean): Either[String, Array[Byte]] = {
    println("get " + url)

    var httpEx: Exception = null

    val httpClientget = new GetMethod(url)
    var responseString = ""
    var responseBytes: Array[Byte] = null

    try {
      val httpClient = new HttpClient()

      val proxyHost = getProxyHost
      if (proxyHost.isDefined){
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost.get);
      }

      httpClient.executeMethod(httpClientget)

      if (responseAsString)
        responseString = httpClientget.getResponseBodyAsString()
      else
        responseBytes = httpClientget.getResponseBody()
    }
    catch{
      case ex: Exception => {
        httpEx = ex
      }
    }
    finally{
      httpClientget.releaseConnection()
    }

    if (httpEx != null) throw httpEx

    if (responseAsString)
      Left(responseString)
    else
      Right(responseBytes)
  }

  /**
   * Post json to a url
   * @param url
   * @param json
   * @return
   */
  def postJson(url: String, json: JsValue): String = {

    println("postJson to " + url)

    var httpEx: Exception = null
    var responseBody = ""
    val postMethod = new PostMethod(url)

    try{
      val httpClient = new HttpClient();

      // use a proxy?
      val proxyHost = getProxyHost
      if (proxyHost.isDefined){
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost.get);
      }

      postMethod.setRequestHeader(new Header("Content-type", "application/json"))
      postMethod.setRequestEntity(new StringRequestEntity(json.toString(), "application/json", null))

      httpClient.executeMethod(postMethod)

      responseBody = postMethod.getResponseBodyAsString()
    }
    catch {
      case e: Exception =>
        httpEx = e
    }
    finally {
      postMethod.releaseConnection
    }

    if (httpEx != null) throw httpEx

    return responseBody
  }

  /**
   * If a proxy is defined in the env vars, return the proxy host, else None
   * @return
   */
  private def getProxyHost: Option[HttpHost] = {
    // use a proxy?
    val proxyHost = Environment.getStr("PROXY_HOST")

    if (proxyHost != None && !proxyHost.isEmpty){
      println("Using proxy host " + proxyHost.get)
      val proxyPort = {
        try { // catch number format exception and default to 8080
          Environment.getInt("PROXY_PORT").getOrElse(8080)
        }
        catch {
          case nfe: NumberFormatException =>
            println("NumberFormatException parsing PROXY_PORT.  Defaulting port to 8080.")
            8080
        }
      }
      Some(new HttpHost(proxyHost.get, proxyPort))
    }
    else {
      None
    }
  }
}
