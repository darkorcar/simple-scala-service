package com.simplescalaservice.server.api

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID

import cats.effect._
import com.simplescalaservice.server.repository.{GreetingsRepository, PageQuery => SqlPageQuery}
import com.simplescalaservice.api._
import tapir.server.akkahttp._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteConcatenation.concat
import com.simplescalaservice.model.Greeting

class ApiRoutes(repository: GreetingsRepository[IO]) {

  def routes: Route =
    concat(routesList: _*)

  val listRoute = Api.list.toRoute {

      case GreetInput(PageQuery(pageNumber, pageSize), older, later) =>
        repository
          .list(SqlPageQuery(pageNumber, pageSize), older, later)
          .map { result =>
            Right(Page(result.rows, pageNumber, pageSize, result.totalPages))
          }
          .unsafeToFuture()
  }

  val createRoute = Api.create.toRoute { createGreet =>
      repository
        .create(Greeting(UUID.randomUUID(), createGreet.greet.value, LocalDateTime.now().toInstant(ZoneOffset.UTC)))
        .map(Right.apply _)
        .unsafeToFuture()
    }

  val routesList = Seq(listRoute, createRoute)

}
