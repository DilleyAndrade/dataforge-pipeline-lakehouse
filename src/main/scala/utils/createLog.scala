package utils

import org.apache.spark.sql.types.TimestampType
import org.apache.spark.sql.{DataFrame, SparkSession}

object createLog {

  // 🔹 criar DataFrame de log
  def create_log_df(
                     spark: SparkSession,
                     logId: String,
                     table_name: String,
                     step: String,
                     status: String,
                     total_lines: Long,
                     message: String ,
                     created_at: TimestampType
                   ): DataFrame = {

    import spark.implicits._

    Seq(( logId, table_name, step, status, total_lines, message, created_at ))
      .toDF( "log_id", "table_name", "step", "status", "total_lines", "message", "created_at"    )
  }
}
