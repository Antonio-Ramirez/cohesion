package com.cohesion.challenge

import com.cohesion.challenge.subscriber.ServiceContext

object ApplicationServer extends ServiceContext {

  def main(args: Array[String]): Unit = {
    logger.info(s"Starting service $nameApplication")
  }

}
