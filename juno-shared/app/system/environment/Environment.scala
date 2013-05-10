package junoshared.system.environment


object Environment {

  /**
   * Get environment variable string
   * @param key
   * @return
   */
  def getStr(key: String): Option[String] = {
    System.getenv(key) match {
      case v:String => Some(v)
      case _ => None
    }
  }

  /**
   * Get environment variable Int.  Throws NumberFormatException if Int parse fails
   * @param key
   */
  def getInt(key: String): Option[Int] = {
    val envStr = getStr(key)
    if (envStr == None) return None;

    return Some(envStr.get.toInt)

    None
  }

  /**
   * Get environment variable Boolean.  Throws BooleanFormatException if boolean parse fails
   * @param key
   * @return
   */
  def getBool(key: String): Option[Boolean] = {
    val envStr = getStr(key)
    if (envStr == None) return None;

    if (envStr.get.toLowerCase.equals("true")){
      return Some(true)
    }
    else if (envStr.get.toLowerCase.equals("false")){
      return Some(false)
    }

    throw new BooleanFormatException
  }

  case class BooleanFormatException() extends Exception {
    override def getMessage() = "Invalid string format for Boolean"
  }
}
