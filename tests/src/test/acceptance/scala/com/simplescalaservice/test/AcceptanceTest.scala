package com.simplescalaservice.test

import cats.effect._
import cats.syntax.all._

import com.softwaremill.sttp._
import org.scalatest._
import scala.concurrent.duration._

import com.simplescalaservice.server._

trait AcceptanceTest extends WordSpecLike with TestFixtures with BeforeAndAfterAll {

  val config: Config = Config.load.unsafeRunSync

  implicit val backend = HttpURLConnectionBackend()

  override def withFixture(test: NoArgTest) = {
    val wrappedTest = IO(super.withFixture(test)) <* IO.sleep(500.millis)

    val program =
      (initializedDatabase(config.database)
        *> runningApp(config)).use { _ =>
        wrappedTest
      }

    program.unsafeRunSync()
  }

}
