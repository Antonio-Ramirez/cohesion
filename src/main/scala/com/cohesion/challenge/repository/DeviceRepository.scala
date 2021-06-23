package com.cohesion.challenge.repository

import java.time.Clock

import com.cohesion.challenge.model.DeviceData
import com.cohesion.challenge.repository.model.RawData
import com.cohesion.challenge.repository.table.RawDataTable
import com.typesafe.scalalogging.LazyLogging
import slick.dbio.{Effect, NoStream}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import slick.sql.FixedSqlAction

import scala.concurrent.{ExecutionContext, Future}

class DeviceRepository(database: Database)
                      (implicit clock: Clock, ec: ExecutionContext)
  extends LazyLogging {

  this: RawDataTable =>

  private def insertDataAndReturn(deviceReading: DeviceData): FixedSqlAction[model.RawData, NoStream, Effect.Write] = {
    val insertQuery = rawData returning rawData.map(_.id) into ((rd, newId) => rd.copy(id = newId))
    insertQuery.+=(RawData.fromModel(deviceReading))
  }

  def insertRawData(deviceReading: DeviceData): Future[Option[Long]] = {

    database.run(insertDataAndReturn(deviceReading)) map { x =>
      logger.info(s"Successfully insert raw data for deviceId=[${x.deviceId}]")
      Some(x.id)
    } recover {
      case e: Exception =>
        logger.error(s"There was an error inserting raw data for deviceId=[${deviceReading.deviceId}], error=[$e]", e)
        None
    }
  }

}
