package com.cohesion.challenge.repository.model

import java.sql.Timestamp
import java.util.UUID

import com.cohesion.challenge.model.DeviceData

case class RawData(
                  id: Long,
                  deviceId: UUID,
                  currentValue: Float,
                  unit: String,
                  timestamp: Timestamp,
                  version: Float
                  )

object RawData {

  def fromModel(deviceReading: DeviceData): RawData =
    RawData(
      0,
      deviceReading.deviceId,
      deviceReading.currentValue,
      deviceReading.unit,
      Timestamp.from(deviceReading.timestamp),
      deviceReading.version
    )

  def tupled: (
    (Long,
      UUID,
      Float,
      String,
      Timestamp,
      Float)
    ) => RawData = (RawData.apply _).tupled
}

