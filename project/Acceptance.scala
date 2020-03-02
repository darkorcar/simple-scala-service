import sbt.Keys._
import sbt._

object Acceptance {

  lazy val AcceptanceTest = config("acceptance").extend(IntegrationTest)

  lazy val acceptanceTestSettings = inConfig(AcceptanceTest)(
    Defaults.testSettings ++ Seq(
      fork := true,
      scalaSource := baseDirectory.value / "src" / "test" / "acceptance",
      resourceDirectory := baseDirectory.value / "src" / "test" / "acceptance" / "resources",
      parallelExecution := false
    ))

  lazy val acceptanceTests: Project => Project = _.configs(AcceptanceTest)
    .settings(acceptanceTestSettings)
}
