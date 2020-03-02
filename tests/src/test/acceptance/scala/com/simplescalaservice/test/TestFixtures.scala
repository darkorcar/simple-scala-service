package com.simplescalaservice.test

import cats.effect._
import cats.implicits._
import com.softwaremill.sttp._
import com.typesafe.scalalogging.LazyLogging

import retry.CatsEffect._
import retry._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

import com.simplescalaservice.server._

trait TestFixtures extends LazyLogging {

  implicit val sc = IO.contextShift(ExecutionContext.Implicits.global)
  implicit val timer = IO.timer(ExecutionContext.Implicits.global)
  
  def initializedDatabase(config: DatabaseConfig): Resource[IO, Unit] =
    for {
      ds <- Database.dataSource(config)(ExecutionContext.global)
      _  <- Resource.make(Migrations.migrate(ds).uncancelable)(_ => Migrations.clean(ds))
    } yield ()

  def runningApp(config: Config): Resource[IO, Unit] =
    app(config) *> Resource.liftF(pollHttp(config.http))

  private def pollHttp(config: HttpConfig) = {
    implicit val backend = HttpURLConnectionBackend()

    val request = IO {
      sttp
        .get(uri"http://localhost:${config.port}")
        .send()
      ()
    }

    val policy =
      RetryPolicies.limitRetriesByCumulativeDelay[IO](
        10 seconds,
        RetryPolicies.constantDelay((250 millis))
      )

    def onError(error: Throwable, details: RetryDetails) = IO {
      logger.debug(s"Error polling application: $error. Retry: $details")
    }

    retryingOnAllErrors(policy, onError _)(request)

  }

  private def app(config: Config): Resource[IO, CancelToken[IO]] =
    Resource.make[IO, CancelToken[IO]] {
      val cancel = Main.runFromConfig(config).runCancelable { _ =>
        IO.unit
      }
      cancel.toIO
    }(identity _)

}
