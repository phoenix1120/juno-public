package junoshared.IO

import java.io.{OutputStream, InputStream}

object File {
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
   * Utility method for copying an InputStream to an OutputStream.
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
