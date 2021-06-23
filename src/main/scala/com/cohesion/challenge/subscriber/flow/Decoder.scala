package com.cohesion.challenge.subscriber.flow

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.cohesion.challenge.model.DeviceData
import com.typesafe.scalalogging.LazyLogging
import io.circe.parser
import org.apache.kafka.clients.consumer.ConsumerRecord

import scala.concurrent.ExecutionContext

class Decoder(implicit ec: ExecutionContext) extends LazyLogging {

  val flow: Flow[ConsumerRecord[String, String], Option[DeviceData], NotUsed] =
    Flow[ConsumerRecord[String, String]]
      .map { record =>
        parser.decode[DeviceData](record.value) match {
          case Left(error) =>
            logger.error(s"Error trying to decode message from deviceId=[${record.key}], error=[${error.getMessage}]", error)
            None
          case Right(data) => Some(data)
        }
      }
}
