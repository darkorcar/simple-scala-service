import Dependencies._
import Acceptance._, Integration._, Unit._

val commonSettings = Seq(
  organization := "com",
  scalaVersion := "2.12.9"
)

lazy val buildInfoSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey]("artifactId" -> name.value,
                                     version,
                                     scalaVersion,
                                     sbtVersion,
                                     "builtOn" -> new java.util.Date()),
  buildInfoPackage := "com.simplescalaservice",
  buildInfoOptions += BuildInfoOption.ToJson
)

lazy val simpleScalaService: Project = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    publishArtifact := false
  )
  .aggregate(core, server)

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "simple-scala-service-core",
    moduleName := "simple-scala-service-core",
    libraryDependencies ++= Seq(
      enumeratum,
      enumeratumCirce,
      circeCore,
      circeGeneric,
      circeRefined,
      refined,
      refinedCats,
      tapirCore,
      tapirJsonCirce
    )
  )

lazy val server = (project in file("server"))
  .configure(unitTests, integrationTests)
  .settings(commonSettings: _*)
  .settings(buildInfoSettings: _*)
  .settings(
    name := "simple-scala-service",
    libraryDependencies ++= loggerDependencies ++ Seq(
      akkaActor,
      akkaStream,
      akkaSlf4j,
      akkaHttp,
      akkaHttpCirce,
      tapirAkkaHttpServer,
      tapirOpenAPI,
      tapirOpenAPICirce,
      swaggerUI,
      pureConfig,
      squants,
      pureConfigSquants,
      doobieCore,
      doobieHikari,
      doobieH2,
      doobieRefined,
      enumeratumDoobie,
      mariaDBJavaClient,
      flywayCore,
      quickLens,
      scalaTagging,
      fuuid,
      akkaTestKit % "test",
      akkaHttpTestKit % "test",
      scalaTest % "it,test",
      scalaMock % "test"
    ) ++ supHealthChecker,
    fork in run := true,
    mainClass in Compile := Some("com.simplescalaservice.server.Main"),
  )
  .dependsOn(core)
  .enablePlugins(BuildInfoPlugin)


val tests = (project in file("tests"))
  .configure(unitTests, integrationTests, acceptanceTests)
  .settings(commonSettings:_*)
  .settings(
    name := "simple-scala-service-tests",
    publishArtifact := false,
    libraryDependencies ++= loggerDependencies ++ Seq(
      quickLens % "acceptance",
      scalaTest % "test,acceptance",
      sttpCore % "acceptance",
    ) ++ catsRetryDependencies.map(_ % "acceptance")
  )
  .dependsOn(server)
