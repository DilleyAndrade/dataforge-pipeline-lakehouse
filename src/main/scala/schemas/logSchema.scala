package schemas

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType, TimestampType}

object logSchema {
  val log_schema = StructType(Array(
    StructField("log_id", StringType, true),
    StructField("table_name", StringType, true),
    StructField("source", StringType, true),
    StructField("target", StringType, true),
    StructField("status", StringType, true),
    StructField("total_lines", IntegerType, true),
    StructField("message", StringType, true),
    StructField("created_at", TimestampType, true)
  ))
}
