package com.simplescalaservice.server
import cats.syntax.all._
import cats.effect._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import com.typesafe.scalalogging.LazyLogging
import com.simplescalaservice.server.repository.GreetingsSqlRepository
import akka.stream.ActorMaterializer
import com.simplescalaservice.server.api.Documentation
import com.simplescalaservice.server.serviceinfo.InfoRoute

final case class Resources(
                            http: HttpServer,
                            transactor: HikariTransactor[IO]
)

object Main extends IOApp with LazyLogging {

  override def run(args: List[String]): IO[ExitCode] =
    Config.load >>= runFromConfig _

  private def resources(config: Config): Resource[IO, Resources] =
    for {
      blockingExecutionContext <- ExecutionContexts.cachedThreadPool[IO](Sync[IO])
      xa                       <- Database.connectionPool(config.database)(blockingExecutionContext)

      system <- Akka.system("simple-scala-service")
      http = new HttpServer(config.http)(system, ActorMaterializer()(system))
    } yield
      Resources(
        http,
        xa
      )

  def runFromConfig(config: Config): IO[ExitCode] = {
    val program = resources(config).use { resources =>
      val apiRoute = new api.ApiRoutes(new GreetingsSqlRepository(resources.transactor))
      val infoRoute = new InfoRoute(resources)
      val swaggerDocsRoute = new SwaggerUI(Documentation.yml)
      val fullRoute = Routes(apiRoute, infoRoute, swaggerDocsRoute)

      Migrations.migrate(resources.transactor) *> resources.http
        .serve(fullRoute)
        .map(_ => ExitCode.Success)
    }

    program.guaranteeCase {
      case ExitCase.Error(e) =>
        IO(logger.error("Error executing program", e))

      case ExitCase.Canceled =>
        IO(logger.info("Interrupted. Exiting"))

      case _ => IO.unit
    }

  }
}
