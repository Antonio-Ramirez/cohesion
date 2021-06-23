package com.cohesion.challenge.subscriber.flow

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.cohesion.challenge.model.DeviceData
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

class Tracker(numOfDevices: Int)(implicit ec: ExecutionContext) extends LazyLogging {

  val flow: Flow[(DeviceData, Option[Long]), DeviceData, NotUsed] =
    Flow[(DeviceData, Option[Long])]
      .groupBy(numOfDevices, data => data._1.deviceId)
      .mapAsync(numOfDevices) { data =>
        val device = data._1
        logger.info(s"Last value receiving from deviceId=[${device.deviceId}] value=[${device.currentValue}]")
        Future.successful(device)
      }
      .mergeSubstreams
}
