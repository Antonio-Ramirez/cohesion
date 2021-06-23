package com.cohesion.challenge.repository.table

import java.sql.Timestamp
import java.util.UUID

import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery, Tag}
import com.cohesion.challenge.repository.model

trait RawDataTable {

  val schema: String
  private val tableName: String = "raw_data"
  protected[this] val rawData: TableQuery[RawData] = TableQuery[RawData]

  private[repository] class RawData(tag: Tag) extends Table[model.RawData](tag, Some(schema), tableName) {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def deviceId: Rep[UUID] = column[UUID]("device_id")
    def currentValue: Rep[Float] = column[Float]("current_value")
    def `unit`: Rep[String] = column[String]("unit")
    def `timestamp`: Rep[Timestamp] = column[Timestamp]("timestamp")
    def version: Rep[Float] = column[Float]("version")

    override def * : ProvenShape[model.RawData] =
      (id,
        deviceId,
        currentValue,
        `unit`,
        `timestamp`,
        version
      ).mapTo[model.RawData]
  }
}
