package com.cohesion.challenge.repository

import java.util.UUID

import com.cohesion.challenge.model.DeviceData
import com.cohesion.challenge.repository.setup.EmbeddedPostgresDBSetup
import com.cohesion.challenge.repository.table.RawDataTable
import org.scalatest.featurespec.AsyncFeatureSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, GivenWhenThen}

class DeviceRepositorySpec extends AsyncFeatureSpec
  with GivenWhenThen
  with Matchers
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with EmbeddedPostgresDBSetup {

  override def beforeAll(): Unit = {
    createRawDataTable
  }

  override def afterAll(): Unit = shutDownDB

  override def beforeEach(): Unit = {
    truncate("raw_data", true)
  }

  lazy val repository: DeviceRepository = new DeviceRepository(devicePostgresDb) with RawDataTable {
    override val schema: String = "public"
  }

  Feature("Create device data") {
    Scenario("Successfully create new device data records") {
      Given("a list of device data objects")

      val deviceId = UUID.randomUUID()
      val deviceData = DeviceData(deviceId = deviceId)

      When("DeviceRepository#insertRawData")
      val result = repository.insertRawData(deviceData)

      Then("the result must be successful")
      result.map {
        case Some(id) =>
          id shouldBe 1
          verifyDeviceDataExists(deviceId) shouldBe true
        case None => fail("Unexpected failure")
      }
    }
  }

}
