package com.asidatascience.configuration

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration._
import scala.util.{Success, Try}

import akka.actor.ActorSystem
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

abstract class BaseSpec
extends FlatSpec
with Matchers
with BeforeAndAfterAll
with ScalaFutures {

  override implicit val patienceConfig = PatienceConfig(
    timeout = 5.seconds, interval = 50.millis)

  implicit protected val actorSystem = ActorSystem()

  override def afterAll(): Unit = {
    actorSystem.terminate().futureValue
    ()
  }

  case class Configuration(timestamp: Long)

  protected val dummyConfiguration = Configuration(1L)
  protected val dummyContents = "dummy-contents"

  class TestConfigurationParser(expectedContents: String) {
    val nHits = new AtomicInteger(0)

    def parse(contents: String): Try[Configuration] = {
      contents shouldEqual expectedContents
      nHits.incrementAndGet()
      val config = dummyConfiguration
      Success(config)
    }
  }

}
