import sbt.Keys._
import sbt._

object Integration {

  lazy val IntegrationTests = config("integration").extend(Test)

  lazy val integrationTestSettings = inConfig(IntegrationTest)(
    Defaults.testSettings ++ Seq(
      fork := true,
      scalaSource := baseDirectory.value / "src" / "test" / "integration",
      resourceDirectory := baseDirectory.value / "src" / "test" / "integration" / "resources",
      parallelExecution := false
    ))

  lazy val integrationTests: Project => Project = _.configs(IntegrationTest)
    .settings(integrationTestSettings)

}
