package com.asidatascience.configuration

import java.io.{File, PrintWriter}
import java.nio.file.{Path, Paths}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

import org.scalatest.concurrent.Eventually
import org.scalatest.Inside

class DynamicConfigurationFromFileSpec
extends BaseSpec
with Eventually
with Inside {

  private def withTemporaryFile(block: Path => Unit): Unit = {
    val file = File.createTempFile("dynamic-configuration", ".tmp")
    val path = Paths.get(file.getAbsolutePath)
    try {
      block(path)
    } finally {
      file.delete()
      ()
    }
  }

  private def newDynamicConfiguration(
    path: Path, parse: String => Try[Configuration]
  ): DynamicConfiguration[Configuration] =
    DynamicConfigurationFromFile(
      path,
      RefreshOptions(100.millis, 300.millis)
    )(parse)

  "DynamicConfigurationFromFile" should "return None initially" in
  withTemporaryFile { path =>
    val parser = new TestConfigurationParser(dummyContents)
    val configuration = newDynamicConfiguration(path, parser.parse)
    configuration.currentConfiguration shouldEqual None
    ()
  }

  it should "register an initial configuration" in withTemporaryFile { path =>
    val parser = new TestConfigurationParser(dummyContents)
    val configuration = newDynamicConfiguration(path, parser.parse)

    actorSystem.scheduler.scheduleOnce(1.second) {
      val file = new PrintWriter(path.toString)
      file.write(dummyContents)
      file.close()
      ()
    }

    eventually {
      parser.nHits.get shouldEqual 1
      inside (configuration.currentConfiguration) {
        case Some(actualConfiguration) =>
          actualConfiguration shouldEqual dummyConfiguration
      }
      ()
    }
  }

}
