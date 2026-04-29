package batch

import connectors.apiConn.getAllData
import io.github.cdimascio.dotenv.Dotenv

object batchApiIngestion {

  private val dotenv = Dotenv.load()
  private val timeout = 10000

  def apiIngestion(): Unit = {
    val url:String = dotenv.get("API_URL")
    val data = getAllData(url, timeout)
  }
}
