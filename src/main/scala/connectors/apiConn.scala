package connectors

object apiConn {

  def getAllData(url: String, timeout: Int): Any = {
    try {

      val response = requests.get(
        url,
        connectTimeout = timeout, // Timeout to access API
        readTimeout = timeout     // Timeout for receiving the data
      )

      if (response.statusCode != 200) {
        s"Error: Failed to get API data (HTTP ${response.statusCode})"
      } else {
        ujson.read(response.text())
      }

    } catch {
      case _: requests.TimeoutException =>
        "Error: The request timed out (API took too long to respond)!"

      case _: requests.RequestFailedException =>
        "Error: Request failed (Connection refused or invalid URL)!"

      case e: Exception =>
        s"Error: Api not connected. An unexpected error occurred: ${e.getMessage}"
    }
  }
}