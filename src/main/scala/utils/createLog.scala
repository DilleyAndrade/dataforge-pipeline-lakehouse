package utils


import org.apache.spark.sql.Row
import schemas.logSchema.log_schema
import org.apache.spark.sql.{DataFrame, SparkSession}

object createLog {

  // 🔹 criar DataFrame de log
  def create_log_df(
                     spark: SparkSession,
                     log_id: String,
                     table_name: String,
                     source: String,
                     target: String,
                     status: String,
                     total_lines: Integer,
                     message: String ,
                     created_at: java.sql.Timestamp
                   ): DataFrame = {


    val log_data = Seq(Row( log_id, table_name, source, target, status, total_lines, message, created_at ))


    spark.createDataFrame(
      spark.sparkContext.parallelize(log_data),
      log_schema
    )

  }
}
