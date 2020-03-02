package com.simplescalaservice.server.api

import com.simplescalaservice.api.Api

object Documentation {

  import tapir.docs.openapi._
  import tapir.openapi.circe.yaml._
  import tapir.openapi.OpenAPI

  val openApi: OpenAPI = List(Api.create, Api.list).toOpenAPI("The tapir library", "0.29.192-beta-RC1")
  val yml: String = openApi.toYaml
}