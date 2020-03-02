package com.simplescalaservice.server

import cats.syntax.all._

import cats.effect.IO

import akka.Done
import akka.actor.ActorSystem

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import akka.stream.ActorMaterializer

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration._

import scala.language.postfixOps

final case class HttpConfig(
    interface: String,
    port: Int
)

class HttpServer(config: HttpConfig)(implicit system: ActorSystem, mat: ActorMaterializer)
    extends LazyLogging {

  import system.dispatcher

  def serve(route: Route): IO[Done] =
    for {
      binding <- IO.fromFuture(IO {
        logger.info(s"binding http server to ${config.interface}:${config.port}")
        Http().bindAndHandle(
          Route.handlerFlow(route),
          interface = config.interface,
          port = config.port)
      })
      _ <- IO.cancelable[Done] { _ =>
        IO {
          logger.info(s"Terminating Http server")
        } *> IO
          .fromFuture(IO {
            binding.terminate(3 seconds)
          })
          .map(_ => ())
      }

    } yield Done

}
