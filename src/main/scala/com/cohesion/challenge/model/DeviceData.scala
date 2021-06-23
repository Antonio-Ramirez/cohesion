package com.cohesion.challenge.model

import java.time.Instant
import java.util.UUID

import io.circe.{Decoder, parser}
import io.circe.generic.semiauto._
import scala.util.Random

case class DeviceData(
                          deviceId: UUID =  UUID.randomUUID(),
                          currentValue: Float = Random.nextFloat(),
                          unit: String = "Fahrenheit",
                          timestamp: Instant = Instant.now(),
                          version: Float = 1.0f
                        ) {

  def sense: DeviceData = {
    DeviceData(deviceId, Random.nextFloat(), unit, Instant.now, version)
  }
}

object DeviceData {

  implicit lazy val deviceDataDec: Decoder[DeviceData] = deriveDecoder[DeviceData]
}
