package com.simplescalaservice.server

import akka.actor.ActorSystem
import cats.effect.{IO, Resource}

import scala.concurrent.ExecutionContext.Implicits.global

object Akka {
  def system(name: String): Resource[IO, ActorSystem] =
    Resource(IO {
      val system = ActorSystem(name)
      (system, IO.fromFuture(IO(system.terminate().map(_ => ()))))
    })
}
