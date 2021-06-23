package com.cohesion.challenge.subscriber

import akka.actor.{Actor, ActorLogging, Props}
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.cohesion.challenge.subscriber.flow.{Decoder, Tracker, Persister}

import scala.concurrent.ExecutionContext

class DataCollector(consumerSettings: ConsumerSettings[String, String], topic: String,
                    decoder: Decoder, persister: Persister, tracker: Tracker)
                   (implicit ec: ExecutionContext, mt: Materializer)
  extends Actor with ActorLogging {

  override def receive: Receive = {
    case DataCollector.Start =>
      log.info("It's time to collect all device ones")

      Consumer.plainSource(consumerSettings, Subscriptions.topics(topic))
        .via(decoder.flow)
        .via(persister.flow)
        .via(tracker.flow)
        .toMat(Sink.seq)(DrainingControl.apply)
        .run()

    case _ => log.error("Message unknown, collector will be discard it")
  }
}

object DataCollector {
  def props(consumerSettings: ConsumerSettings[String, String],
            topic: String,
            decodeFlow: Decoder,
            persistFlow: Persister,
            lastValuesFlow: Tracker)
           (implicit ec: ExecutionContext, mt: Materializer): Props = {
    Props(new DataCollector(consumerSettings, topic, decodeFlow, persistFlow, lastValuesFlow))
  }

  case class Start()
}
