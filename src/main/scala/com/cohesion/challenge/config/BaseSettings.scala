package com.cohesion.challenge.config

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

trait BaseSettings {
  lazy val config: Config = ConfigFactory
    .load(getClass.getClassLoader)
    .withFallback(ConfigFactory.defaultReference(getClass.getClassLoader))

  lazy val logger: Logger = Logger(LoggerFactory.getLogger(getClass.getName))
}
