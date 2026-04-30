package utils

import org.apache.spark.sql.SparkSession

object sparkSession {
  def spark_session(app_name: String): SparkSession = {
    SparkSession.builder
      .appName(app_name)
      .master("local[*]")
      .getOrCreate()
  }
}
