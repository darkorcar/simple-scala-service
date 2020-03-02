package com.simplescalaservice.server

class SwaggerUI(yml: String) {

  import akka.http.scaladsl.model.StatusCodes
  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.server.Route
  import java.util.Properties

  val DocsYml = "docs.yml"

  private val redirectToIndex: Route =
    redirect(s"/docs/index.html?url=/docs/$DocsYml", StatusCodes.PermanentRedirect) //

  private val swaggerUiVersion = {
    val p = new Properties()
    p.load(getClass.getResourceAsStream("/META-INF/maven/org.webjars/swagger-ui/pom.properties"))
    p.getProperty("version")
  }

  val routes: Route =
    path("docs") {
      redirectToIndex
    } ~
      pathPrefix("docs") {
        path("") { // this is for trailing slash
          redirectToIndex
        } ~
          path(DocsYml) {
            complete(yml)
          } ~
          getFromResourceDirectory(s"META-INF/resources/webjars/swagger-ui/$swaggerUiVersion/")
      }
}
