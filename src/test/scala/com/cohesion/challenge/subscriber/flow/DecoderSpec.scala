package com.cohesion.challenge.subscriber.flow

import akka.stream.scaladsl.{Sink, Source}
import com.cohesion.challenge.ServiceContextMock
import org.scalatest.featurespec.AsyncFeatureSpecLike
import org.scalatest.matchers.should.Matchers

class DecoderSpec extends ServiceContextMock
  with AsyncFeatureSpecLike
  with Matchers
  with RecordFixture {

  Feature("Decoder Flow#flow") {
    Scenario("When a right message is received") {

      Source
        .single(record)
        .via(decoder.flow)
        .runWith(Sink.head)
        .map {
          case Some(data) =>
            data shouldBe deviceData
            data.deviceId.toString shouldBe deviceId

          case _ => fail(s"Should not have failed")
        }
    }

    Scenario("When an unknown message is received") {

      Source
        .single(recordWithWrongValue)
        .via(decoder.flow)
        .runWith(Sink.head)
        .map( result => result shouldBe None )
    }
  }
}
