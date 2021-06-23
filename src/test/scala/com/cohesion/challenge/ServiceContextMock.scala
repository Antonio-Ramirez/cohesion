package com.cohesion.challenge

import java.time.Clock

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import akka.testkit.TestProbe
import com.cohesion.challenge.repository.DeviceRepository
import com.cohesion.challenge.repository.table.RawDataTable
import com.cohesion.challenge.subscriber.flow.{Decoder, Persister, Tracker}
import org.scalamock.scalatest.AsyncMockFactory
import slick.jdbc.JdbcBackend

import scala.concurrent.ExecutionContextExecutor

trait ServiceContextMock extends ServiceContext with AsyncMockFactory {

  // Akka
  override implicit def system: ActorSystem = ActorSystem("test")
  override implicit def materializer: Materializer = Materializer(system)
  override implicit def execCtx: ExecutionContextExecutor = system.dispatcher

  override implicit lazy val clock: Clock = Clock.systemUTC()

  // PostgresDb
  override lazy val postgresDb: JdbcBackend.Database = mock[JdbcBackend.Database]

  class DeviceRepositoryMock extends DeviceRepository(postgresDb) with RawDataTable {
    override val schema: String = "devices"
  }
  override lazy val repository: DeviceRepository = mock[DeviceRepositoryMock]

  // Flows
  override lazy val decoder: Decoder = new Decoder()
  override lazy val persister: Persister = new Persister(5, repository)
  override lazy val tracker: Tracker = new Tracker(5)

  // Actors

  override lazy val sensorActor: ActorRef = TestProbe().ref
  override lazy val collectorActor: ActorRef = TestProbe().ref
}
