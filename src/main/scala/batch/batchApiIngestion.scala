package batch

import connectors.apiConn.getAllData
import connectors.minioConn.minio_connection
import io.github.cdimascio.dotenv.Dotenv

object batchApiIngestion {

  // variables
  private val dotenv = Dotenv.load()
  private val timeout = 10000

  private val minio_endpoint = dotenv.get("MINIO_ENDPOINT")
  private val minio_access_key = dotenv.get("MINIO_ACCESS_KEY")
  private val minio_secret_key = dotenv.get("MINIO_SECRET_KEY")


  def apiIngestion(): Unit = {
    // Api connection
    val url:String = dotenv.get("API_URL")
    val data = getAllData(url, timeout)

    // Minio connection
    minio_connection(minio_endpoint, minio_access_key, minio_secret_key)
  }
}
