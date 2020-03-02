import sbt._

object Dependencies {

  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  lazy val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  lazy val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  lazy val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion
  lazy val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion

  lazy val enumeratum = "com.beachape" %% "enumeratum" % enumeratumVersion

  lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
  lazy val squants = "org.typelevel" %% "squants" % squantsVersion
  lazy val pureConfigSquants = "com.github.pureconfig" %% "pureconfig-squants" % pureConfigSquantsVersion

  lazy val mariaDBJavaClient = "org.mariadb.jdbc" % "mariadb-java-client" % mariaDBDriverVersion
  lazy val doobieCore = "org.tpolecat" %% "doobie-core" % doobieVersion
  lazy val doobieHikari = "org.tpolecat" %% "doobie-hikari" % doobieVersion
  lazy val doobieH2 = "org.tpolecat" %% "doobie-h2" % doobieVersion
  lazy val doobieRefined = "org.tpolecat" %% "doobie-refined" % doobieVersion
  lazy val enumeratumDoobie = "com.beachape" %% "enumeratum-doobie" % enumeratumDoobieVersion

  lazy val flywayCore = "org.flywaydb" % "flyway-core" % flywayVersion

  lazy val loggerDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "ch.qos.logback" % "logback-core" % "1.2.3",
    "net.logstash.logback" % "logstash-logback-encoder" % "6.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  )

  lazy val catsCore = "org.typelevel" %% "cats-core" % catsVersion
  lazy val catsRetryDependencies = Seq(
    "com.github.cb372" %% "cats-retry-core" % catsRetryVersion,
    "com.github.cb372" %% "cats-retry-cats-effect" % catsRetryVersion
  )

  lazy val circeCore = "io.circe" %% "circe-core" % circeVersion
  lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  lazy val circeLiteral = "io.circe" %% "circe-literal" % circeVersion
  lazy val circeOptics = "io.circe" %% "circe-optics" % circeOpticsVersion
  lazy val circeRefined = "io.circe" %% "circe-refined" % circeOpticsVersion
  lazy val enumeratumCirce = "com.beachape" %% "enumeratum-circe" % enumeratumCirceVersion

  lazy val sttpCore = "com.softwaremill.sttp" %% "core" % sttpVersion
  lazy val sttpCirce = "com.softwaremill.sttp" %% "circe" % sttpVersion

  lazy val tapirCore = "com.softwaremill.tapir" %% "tapir-core" % tapirVersion
  lazy val tapirJsonCirce = "com.softwaremill.tapir" %% "tapir-json-circe" % tapirVersion
  lazy val tapirSttpClient = "com.softwaremill.tapir" %% "tapir-sttp-client" % tapirVersion
  lazy val tapirAkkaHttpServer = "com.softwaremill.tapir" %% "tapir-akka-http-server" % tapirVersion
  lazy val tapirOpenAPI = "com.softwaremill.tapir" %% "tapir-openapi-docs" % tapirVersion
  lazy val tapirOpenAPICirce = "com.softwaremill.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion
  lazy val swaggerUI = "org.webjars" % "swagger-ui" % "3.22.0"

  lazy val quickLens = "com.softwaremill.quicklens" %% "quicklens" % quickLensVersion
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
  lazy val scalaMock = "org.scalamock" %% "scalamock" % scalaMockVersion

  lazy val scalaTagging = "com.softwaremill.common" %% "tagging" % scalaTaggingVerssion
  lazy val fuuid = "io.chrisdavenport" %% "fuuid" % fuuiVersion

  lazy val supHealthChecker = Seq(
    "com.kubukoz" %% "sup-core" % supVersion,
    "com.kubukoz" %% "sup-doobie" % supVersion,
    "com.kubukoz" %% "sup-circe" % supVersion,
  )

  lazy val refined = "eu.timepit" %% "refined" % refinedVersion
  lazy val refinedCats = "eu.timepit" %% "refined-cats" % refinedVersion

  val akkaVersion = "2.5.22"
  val akkaHttpVersion = "10.1.8"
  val pureConfigVersion = "0.10.2"
  val squantsVersion = "1.3.0"
  val pureConfigSquantsVersion = "0.11.1"
  val doobieVersion = "0.6.0"
  val catsVersion = "1.6.0"
  val mariaDBDriverVersion = "1.5.2"
  val sttpVersion = "1.5.14"
  val scalaTestVersion = "3.0.5"
  val flywayVersion = "5.2.4"
  val quickLensVersion = "1.4.12"
  val catsRetryVersion = "0.2.5"

  val enumeratumVersion = "1.5.13"
  val enumeratumDoobieVersion = "1.5.14"
  val enumeratumCirceVersion = "1.5.21"
  val circeVersion = "0.11.1"
  val circeOpticsVersion = "0.11.0"
  val akkaHttpCirceVersion = "1.25.2"
  val scalaMockVersion = "4.1.0"
  val scalaTaggingVerssion = "2.2.1"
  val fuuiVersion = "0.2.0"
  val supVersion = "0.5.0"

  val refinedVersion = "0.9.8"
  val tapirVersion = "0.11.1"
}
