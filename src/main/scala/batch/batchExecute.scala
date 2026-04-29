package batch

import batchApiIngestion.apiIngestion

object batchExecute {
  def main(args:Array[String]): Unit = {
    apiIngestion()
  }
}
