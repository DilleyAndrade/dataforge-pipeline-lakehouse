package utils

import org.apache.spark.sql.DataFrame

object saveMinio {
  def save_minio(
    dataframe: DataFrame,
    save_mode: String,
    format: String,
    path: String
  ): Unit = {
    dataframe.write
      .mode(save_mode)
      .format(format)
      .save(path)
  }
}
