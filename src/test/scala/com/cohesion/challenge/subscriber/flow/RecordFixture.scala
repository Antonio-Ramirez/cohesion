package com.cohesion.challenge.subscriber.flow

import java.time.Instant

import com.cohesion.challenge.model.DeviceData
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.util.UUID

trait RecordFixture {

  val deviceId = "ed4da78a-d7e0-4e1f-8fb5-58196c9bf865"
  val message =
    """
      |{"deviceId":"ed4da78a-d7e0-4e1f-8fb5-58196c9bf865","currentValue":0.46049,"unit":"Fahrenheit","timestamp":"2021-06-23T18:22:36.931Z","version":1.0}
      |""".stripMargin

  val wrongMessage =
    """
      |{}
      |""".stripMargin

  val record: ConsumerRecord[String, String] =
    new ConsumerRecord(
      "test",
      0,
      0,
      deviceId,
      message)

  val recordWithWrongValue: ConsumerRecord[String, String] =
    new ConsumerRecord(
      "test",
      0,
      0,
      deviceId,
      wrongMessage)

  val deviceData: DeviceData = DeviceData(
    deviceId = UUID.fromString("ed4da78a-d7e0-4e1f-8fb5-58196c9bf865"),
    currentValue = 0.46049f,
    timestamp = Instant.parse("2021-06-23T18:22:36.931Z")
  )
}
