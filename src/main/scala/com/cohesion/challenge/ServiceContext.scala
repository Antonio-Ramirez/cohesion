package com.cohesion.challenge

import java.time.{Clock, ZoneId}

import akka.actor.{ActorRef, ActorSystem}
import akka.kafka.{ConsumerSettings, ProducerSettings}
import akka.stream.Materializer
import com.cohesion.challenge.config.Settings
import com.cohesion.challenge.model.DeviceData
import com.cohesion.challenge.publisher.DeviceSensor
import com.cohesion.challenge.repository.DeviceRepository
import com.cohesion.challenge.repository.table.RawDataTable
import com.cohesion.challenge.subscriber.DataCollector
import com.cohesion.challenge.subscriber.flow.{Decoder, Persister, Tracker}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.Duration

trait ServiceContext extends Settings {

  // Akka
  implicit def system: ActorSystem = ActorSystem(nameApplication)
  implicit def materializer: Materializer = Materializer(system)
  implicit def execCtx: ExecutionContextExecutor = system.dispatcher
  implicit lazy val clock: Clock = Clock.system(ZoneId.of(timeZone))

  // PostgresDb
  lazy val postgresDb: JdbcBackend.Database = Database.forConfig("postgres-db", ConfigFactory.load())
  lazy val repository: DeviceRepository = new DeviceRepository(postgresDb) with RawDataTable {
    override val schema: String = "devices"
  }

  // Kafka settings
  val producerSettings: ProducerSettings[String, String] = ProducerSettings(
    system, new StringSerializer, new StringSerializer
  )
    .withBootstrapServers(brokers)

  val consumerSettings: ConsumerSettings[String, String] = ConsumerSettings(
    system, new StringDeserializer, new StringDeserializer
  )
    .withBootstrapServers(brokers)
    .withGroupId(kafkaGroupId)
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset)

  // Devices
  val devices: List[DeviceData] = (1 to numOfDevicesToSense).map(_ => DeviceData()).toList

  // Flows
  lazy val decoder: Decoder = new Decoder()
  lazy val persister: Persister = new Persister(numOfDevicesToSense, repository)
  lazy val tracker: Tracker = new Tracker(numOfDevicesToSense)

  // Producer Actor
  lazy val sensorActor: ActorRef = system
    .actorOf(DeviceSensor.props(producerSettings, kafkaTopic, devices))
  system.scheduler.scheduleAtFixedRate(Duration.Zero, intervalToSense, sensorActor, DeviceSensor.Start)

  // Collector Actor
  lazy val collectorActor: ActorRef = system
    .actorOf(DataCollector.props(consumerSettings, kafkaTopic, decoder, persister, tracker))
  system.scheduler.scheduleOnce(Duration.Zero, collectorActor, DataCollector.Start)
}
