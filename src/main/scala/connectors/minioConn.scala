package connectors

import io.minio.MinioClient
import org.apache.spark.sql.SparkSession


object minioConn {

  def minio_connection(spark:SparkSession, minio_endpoint:String, minio_access_key:String, minio_secret_key:String): Unit = {

    val endpoint = minio_endpoint
    val accessKey = minio_access_key
    val secretKey = minio_secret_key

    val hadoopConf = spark.sparkContext.hadoopConfiguration

    hadoopConf.set("fs.s3a.access.key", accessKey)
    hadoopConf.set("fs.s3a.secret.key", secretKey)
    hadoopConf.set("fs.s3a.endpoint", endpoint)
    hadoopConf.set("fs.s3a.path.style.access", "true")
    hadoopConf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")

    val line_spaces = "="*60

    try {
      val client = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(accessKey, secretKey)
        .build()

      // Teste de conexão
      val buckets = client.listBuckets()

      println(line_spaces)
      println("MinIO connection successfully!")
      println(s"Buckets found: ${buckets.size()}")
      println(line_spaces)

    } catch {
      case e: Exception =>
        println(line_spaces)
        println("Failed to connec to with MINIO")
        println(s"Erro: ${e.getMessage}")
        println(line_spaces)
    }
  }
}
