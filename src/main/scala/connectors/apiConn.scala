package connectors

object apiConn {

  def getAllData(url: String, timeout: Int, apiName:String): Any = {

    val line_spaces = "="*60

    try {

      val response = requests.get(
        url,
        connectTimeout = timeout, // Timeout to access API
        readTimeout = timeout     // Timeout for receiving the data
      )

      if (response.statusCode != 200) {
        s"Error: Failed to get API(${apiName}) data (HTTP ${response.statusCode})"
      } else {
        println(line_spaces)
        println(s"API(${apiName}) connection successfully!")
        ujson.read(response.text())
      }

    } catch {
      case _: requests.TimeoutException =>
        println(line_spaces)
        s"Error: The request timed out (API(${apiName}) took too long to respond)!"

      case _: requests.RequestFailedException =>
        println(line_spaces)
        s"Error: Request failed (API(${apiName}) connection refused or invalid URL)!"

      case e: Exception =>
        println(line_spaces)
        s"Error: API(${apiName}) not connected. An unexpected error occurred: ${e.getMessage}"
    }
  }
}