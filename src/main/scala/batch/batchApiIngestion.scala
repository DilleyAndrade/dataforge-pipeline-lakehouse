package batch

import java.time.LocalDate
import connectors.apiConn.getAllData
import utils.sparkSession.spark_session
import io.github.cdimascio.dotenv.Dotenv
import connectors.minioConn.minio_connection


object batchApiIngestion {

  // variables
  val table_name = "transactions"
  val spark = spark_session("api_ingestion")

  val bucket_bronze = "bronze"
  val bucket_logs = "logs"

  import spark.implicits._

  private val dotenv = Dotenv.load()
  private val timeout = 10000

  val today = LocalDate.now()

  val year  = today.getYear
  val month = f"${today.getMonthValue}%02d"
  val day   = f"${today.getDayOfMonth}%02d"

  private val minio_endpoint = dotenv.get("MINIO_ENDPOINT")
  private val minio_access_key = dotenv.get("MINIO_ACCESS_KEY")
  private val minio_secret_key = dotenv.get("MINIO_SECRET_KEY")


  def apiIngestion(): Unit = {
    // Api connection
    val url:String = dotenv.get("API_URL")
    val data = getAllData(url, timeout, table_name)

    // Minio connection
    minio_connection(spark, minio_endpoint, minio_access_key, minio_secret_key)

    try {
      spark.createDataset(Seq(data))
        .write
        .mode("append")
        .text(s"s3a://${bucket_bronze}/${table_name}/ingestion_date=${today}")

      println(s"File ${table_name} saved successfully in MINIO.")
    } catch {
      case e: Exception => println(s"Failed to save file ${table_name}: ${e.getMessage}")
    }

  }
}
