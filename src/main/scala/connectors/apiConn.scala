package connectors

object apiConn {

  def getAllData(url: String, timeout: Int, api_name: String): String = {

    val line_spaces = "="*60

    try {

      val response = requests.get(
        url,
        connectTimeout = timeout, // Timeout to access API
        readTimeout = timeout     // Timeout for receiving the data
      )

      if (response.statusCode != 200) {
        throw new RuntimeException(
          s"Failed to get API(${api_name}) data (HTTP ${response.statusCode})"
        )
      } else {
        println(line_spaces)
        println(s"API(${api_name}) connection successfully!")
        response.text()
      }

    } catch {
      case _: requests.TimeoutException =>
        println(line_spaces)
        s"Error: The request timed out (API(${api_name}) took too long to respond)!"

      case _: requests.RequestFailedException =>
        println(line_spaces)
        s"Error: Request failed (API(${api_name}) connection refused or invalid URL)!"

      case e: Exception =>
        println(line_spaces)
        s"Error: API(${api_name}) not connected. An unexpected error occurred: ${e.getMessage}"
    }
  }
}