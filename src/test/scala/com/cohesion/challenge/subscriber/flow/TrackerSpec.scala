package com.cohesion.challenge.subscriber.flow

import akka.stream.scaladsl.{Sink, Source}
import com.cohesion.challenge.ServiceContextMock
import org.scalatest.featurespec.AsyncFeatureSpecLike
import org.scalatest.matchers.should.Matchers

class TrackerSpec extends ServiceContextMock
  with AsyncFeatureSpecLike
  with Matchers
  with RecordFixture {

  Feature("Tracker Flow#flow") {
    Scenario("Tracking the last values from each device") {

      Source
        .single(deviceData, Some(323l))
        .via(tracker.flow)
        .runWith(Sink.head)
        .map { result => result shouldBe deviceData }
    }
  }
}
