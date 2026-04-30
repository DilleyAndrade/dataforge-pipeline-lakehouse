package connectors

import io.github.cdimascio.dotenv.Dotenv
import io.minio.MinioClient

object minioConn {
  private val dotenv = Dotenv.load()

  def minio_connection(minio_endpoint:String, minio_access_key:String, minio_secret_key:String): Unit = {

    val endpoint = minio_endpoint
    val accessKey = minio_access_key
    val secretKey = minio_secret_key

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
