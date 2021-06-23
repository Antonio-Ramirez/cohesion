package com.cohesion.challenge

object ApplicationServer extends ServiceContext {

  def main(args: Array[String]): Unit = {
    logger.info(s"Starting service $nameApplication")
  }
}
