package com.cohesion.challenge.repository.setup

import java.sql.Connection
import java.util.UUID

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import javax.sql.DataSource
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

trait EmbeddedPostgresDBSetup {
  lazy val postgres: EmbeddedPostgres = EmbeddedPostgres.start()
  lazy val dataSource: DataSource = postgres.getPostgresDatabase()
  private[setup] lazy val conn: Connection = dataSource.getConnection
  protected lazy val devicePostgresDb: JdbcBackend.Database = Database.forDataSource(dataSource, Some(1))

  try {
    conn.createStatement().execute("""CREATE SCHEMA IF NOT EXISTS public AUTHORIZATION postgres;""")
    conn.createStatement().execute("""GRANT ALL ON SCHEMA public TO postgres WITH GRANT OPTION;""")
    conn.createStatement().execute("""ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO postgres WITH GRANT OPTION;""")
    conn.createStatement().execute("""CREATE EXTENSION pgcrypto;""")
  }
  catch {
    case e: Exception =>
      e.printStackTrace()
      shutDownDB
  }

  protected def shutDownDB: Unit = {
    conn.close()
    postgres.close()
  }

  protected def truncate(tableName: String, cascade: Boolean = false): AnyVal = {
    try {
      conn.createStatement().execute(
        s"""TRUNCATE TABLE $tableName """ + {
          if (cascade) """ CASCADE;""" else ";"
        })
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
        shutDownDB
    }
  }

  protected def createRawDataTable: AnyVal = {
    try {
      conn.createStatement().execute(
        """CREATE TABLE IF NOT EXISTS raw_data
          |(
          |    id serial NOT NULL,
          |    device_id uuid NOT NULL,
          |    current_value numeric(6,4) NOT NULL,
          |    unit character varying(50) NOT NULL,
          |    "timestamp" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
          |    version numeric(3,2) NOT NULL
          |);""".stripMargin)
      conn.createStatement().execute("""alter table raw_data owner to postgres;""")
      conn.createStatement().execute("""GRANT ALL ON TABLE raw_data TO postgres;""")
    }
    catch {
      case e: Exception =>
        e.printStackTrace()
        shutDownDB
    }
  }

  protected def verifyDeviceDataExists(id: UUID): Boolean = {
    val statement = conn.createStatement()
    val rs = statement.executeQuery(s"SELECT device_id FROM raw_data WHERE device_id = '$id';")
    if (rs.next()) {
      statement.getResultSet.getObject(1).asInstanceOf[UUID] == id
    }
    else {
      false
    }
  }
}
