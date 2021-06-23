package com.cohesion.challenge.subscriber.flow

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.cohesion.challenge.model.DeviceData
import com.cohesion.challenge.repository.DeviceRepository
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext

class Persister(numOfDevices: Int, repository: DeviceRepository)(implicit ec: ExecutionContext) extends LazyLogging {

  val flow: Flow[Option[DeviceData], (DeviceData, Option[Long]), NotUsed] =
    Flow[Option[DeviceData]]
      .collect { case Some(data) => data }
      .mapAsync(numOfDevices) { record =>
        for {
          id <- repository.insertRawData(record)
        } yield record -> id
      }
}
