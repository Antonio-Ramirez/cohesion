package com.cohesion.challenge.config

import scala.concurrent.duration.{FiniteDuration, _}

trait Settings extends BaseSettings {

  val nameApplication: String         = config.getString("appName")
  val timeZone: String                = config.getString("time-zone")
  val numOfDevicesToSense: Int        = config.getInt("akka.devicesToSense")
  val intervalToSense: FiniteDuration = config.getDuration("akka.intervalDuration").getSeconds.seconds
  val brokers: String                 = config.getString("kafka.brokers")
  val kafkaTopic: String              = config.getString("kafka.topic")
  val kafkaGroupId: String            = config.getString("kafka.groupId")
  val autoOffsetReset: String         = config.getString("kafka.autoOffsetReset")
}
