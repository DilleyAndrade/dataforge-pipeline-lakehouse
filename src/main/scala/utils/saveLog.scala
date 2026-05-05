package utils

import utils.saveMinio.save_minio
import org.apache.spark.sql.DataFrame
import constantsPath.constantsPath.PATH_SAVE_LOG_FILE_TRANSACTIONS

object saveLog {
  def save_log(df: DataFrame, table_name: String):Unit = {

    try{
      save_minio(df, "append", "parquet", PATH_SAVE_LOG_FILE_TRANSACTIONS)
      println(s"File Log ${table_name} saved successfully in MINIO.")
    } catch {
      case e: Exception => println(s"Failed to save file Log ${table_name}: ${e.getMessage}")
    }
  }
}
