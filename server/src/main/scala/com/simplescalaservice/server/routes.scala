package com.simplescalaservice.server

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.RouteConcatenation.concat
import com.simplescalaservice.server.serviceinfo.InfoRoute

object Routes {
  def apply(apiRoutes: api.ApiRoutes, infoRoute: InfoRoute, swaggerUI: SwaggerUI): Route =
    Route.seal(concat(apiRoutes.routes, infoRoute.routes, swaggerUI.routes))

}
