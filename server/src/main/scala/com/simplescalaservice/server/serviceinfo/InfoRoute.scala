package com.simplescalaservice.server.serviceinfo

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.{ContextShift, IO, Timer}
import com.simplescalaservice.BuildInfo
import com.simplescalaservice.server.Resources
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import sup.modules.circe._

class InfoRoute(resources: Resources)(implicit cs: ContextShift[IO], t: Timer[IO]) {
  def routes: Route =
    pathPrefix("_info") {
      concat(
        // GET host/_info/version
        (get & path("version")) {
          complete {
            HttpResponse(
              status = OK,
              entity = HttpEntity(
                ContentTypes.`application/json`,
                BuildInfo.toJson
              )
            )
          }
        },
        // GET host/_info/health
        (get & path("health")) {
          complete {
            HealthChecks
              .report(resources)
              .unsafeToFuture()
          }
        },
      )
    }
}
