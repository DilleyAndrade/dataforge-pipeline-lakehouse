package connectors

import io.minio.MinioClient
import org.apache.spark.sql.SparkSession
import io.github.cdimascio.dotenv.Dotenv

object minioConn {
  def minio_connection(spark:SparkSession, minio_endpoint:String): Unit = {

    val dotenv = Dotenv.load()
    val minio_access_key = dotenv.get("MINIO_ACCESS_KEY")
    val minio_secret_key = dotenv.get("MINIO_SECRET_KEY")

    val endpoint = minio_endpoint

    val hadoopConf = spark.sparkContext.hadoopConfiguration

    hadoopConf.set("fs.s3a.access.key", minio_access_key)
    hadoopConf.set("fs.s3a.secret.key", minio_secret_key)
    hadoopConf.set("fs.s3a.endpoint", endpoint)
    hadoopConf.set("fs.s3a.path.style.access", "true")
    hadoopConf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")

    val line_spaces = "="*60

    try {
      val client = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(minio_access_key, minio_secret_key)
        .build()

      // Connection test
      val buckets = client.listBuckets()

      println(line_spaces)
      println("MINIO connection successfully!")
      println(s"Buckets found: ${buckets.size()}")

    } catch {
      case e: Exception =>
        println(line_spaces)
        println("Failed to connect to with MINIO")
        println(s"Erro: ${e.getMessage}")
    }
  }
}
