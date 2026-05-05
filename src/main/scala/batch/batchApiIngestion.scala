package batch

import java.sql.Timestamp
import utils.saveLog.save_log
import java.time.LocalDateTime
import utils.saveMinio.save_minio
import utils.createLog.create_log_df
import connectors.apiConn.getAllData
import utils.sparkSession.spark_session
import io.github.cdimascio.dotenv.Dotenv
import connectors.minioConn.minio_connection
import constantsPath.constantsPath.PATH_SAVE_JSON_FILE_TRANSACTIONS

object batchApiIngestion {

  // variables
  val spark = spark_session("api_ingestion")
  private val dotenv = Dotenv.load()
  private val timeout = 10000 //10 seconds

  // Log Informations
  val table_name = "transactions"
  val actual_timestamp = Timestamp.valueOf(LocalDateTime.now())
  val log_id = s"${table_name}-${actual_timestamp}"
  val source = s"Api(${table_name})"
  val target = "bronze"


  // MINIO credentials
  private val minio_endpoint = dotenv.get("MINIO_ENDPOINT")

  def apiIngestion(): Unit = {

    import spark.implicits._

    // Api connection
    val url:String = dotenv.get("API_URL")
    val data = getAllData(url, timeout, table_name)

    // Minio connection
    minio_connection(spark, minio_endpoint)

    //Convert data to JSON
    val df_transaction_json = spark.read.json(Seq(data).toDS())
    val total_lines = df_transaction_json.count().toInt
    val total_columns = df_transaction_json.columns.length

    //Tratamento para não trazer tabela com erro
    if (total_columns <= 1) {
      val status = "failed"
      val message = "Error to get data"
      val total_lines = 0
      val log_table = create_log_df(spark, log_id, table_name, source, target, status, total_lines, message, actual_timestamp)
      save_log(log_table, table_name)

    } else {
      df_transaction_json.show()

      try{
        save_minio(df_transaction_json, "append", "json", PATH_SAVE_JSON_FILE_TRANSACTIONS)
        println(s"File ${table_name} saved successfully in MINIO.")
        val status = "success"
        val message = ""
        val log_table = create_log_df(spark, log_id, table_name, source, target, status, total_lines, message, actual_timestamp)
        save_log(log_table, table_name)

      } catch {
        case e: Exception =>
          println(s"Failed to save file ${table_name}: ${e.getMessage}")
      }
    }

    spark.stop()
  }
}
