package batch

import java.time.LocalDate
import connectors.apiConn.getAllData
import utils.sparkSession.spark_session
import io.github.cdimascio.dotenv.Dotenv
import connectors.minioConn.minio_connection
import utils.saveMinio.save_minio

object batchApiIngestion {

  // variables
  val table_name = "transactions"
  val spark = spark_session("api_ingestion")

  import spark.implicits._

  private val dotenv = Dotenv.load()
  private val timeout = 10000

  val actual_date = LocalDate.now()

  val path_json_file = s"s3a://bronze/${table_name}/ingestion_date=${actual_date}"
  val path_log_file = s"s3a://logs/ingestion_date=${actual_date}"

  private val minio_endpoint = dotenv.get("MINIO_ENDPOINT")
  private val minio_access_key = dotenv.get("MINIO_ACCESS_KEY")
  private val minio_secret_key = dotenv.get("MINIO_SECRET_KEY")


  def apiIngestion(): Unit = {
    // Api connection
    val url:String = dotenv.get("API_URL")
    val data = getAllData(url, timeout, table_name)

    // Minio connection
    minio_connection(spark, minio_endpoint, minio_access_key, minio_secret_key)

    val df_transaction_json = spark.read.json(Seq(data).toDS())

    try{
      save_minio(df_transaction_json, "append", "json", path_json_file)
      println(s"File ${table_name} saved successfully in MINIO.")
    } catch {
      case e: Exception => println(s"Failed to save file ${table_name}: ${e.getMessage}")
    }

  }
}
