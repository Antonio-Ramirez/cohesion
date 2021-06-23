package com.cohesion.challenge.subscriber.flow

import akka.stream.scaladsl.{Sink, Source}
import com.cohesion.challenge.ServiceContextMock
import com.cohesion.challenge.model.DeviceData
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AsyncFeatureSpecLike
import org.scalatest.matchers.should.Matchers

import scala.concurrent.Future

class PersisterSpec extends ServiceContextMock
  with AsyncFeatureSpecLike
  with Matchers
  with GivenWhenThen
  with RecordFixture {

  Feature("Tracker Flow#flow") {
    Scenario("Tracking the last values from each device") {
      Given("A decoded Device Data message")

      When("This is passed into Persisted Flow")
      (repository.insertRawData(_: DeviceData)).expects(*).returning(Future.successful(Some(22L)))

      Then("Flow should result in a new Id from persisted record on DB")
      Source
        .single(Option(deviceData))
        .via(persister.flow)
        .runWith(Sink.head)
        .map { result =>
          result._1 shouldBe deviceData
          result._2 shouldBe Some(22L)
        }
    }
  }
}
