package com.cohesion.challenge.publisher

import akka.actor.{Actor, ActorLogging, Props}
import akka.kafka.ProducerSettings
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.ExecutionContext
import akka.kafka.scaladsl.Producer
import com.cohesion.challenge.model.DeviceData
import io.circe.generic.auto._
import io.circe.syntax._

class DeviceSensor(producerSettings: ProducerSettings[String, String], topic: String, devices: List[DeviceData])
                  (implicit ec: ExecutionContext, mt: Materializer)
  extends Actor with ActorLogging {

  override def receive: Receive = {
    case DeviceSensor.Start =>
      log.info("It's time to scan all device ones")
      Source(devices)
        .map { device =>
          log.info(s"sending current device ${device.deviceId.toString} ${device.sense.asJson.noSpaces} to topic $topic")
          new ProducerRecord[String, String](topic, device.deviceId.toString, device.sense.asJson.noSpaces)
        }
        .runWith(Producer.plainSink(producerSettings))

    case _ => log.error("Message unknown, sensor will be discard it")
  }
}

object DeviceSensor {
  def props(producerSettings: ProducerSettings[String, String], topic: String, devices: List[DeviceData])
           (implicit ec: ExecutionContext, mt: Materializer): Props = {
    Props(new DeviceSensor(producerSettings, topic, devices))
  }

  case class Start()
}
