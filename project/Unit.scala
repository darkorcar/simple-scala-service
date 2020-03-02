import sbt.Keys._
import sbt._

object Unit {

  lazy val unitTestSettings = inConfig(Test)(
    Defaults.testSettings ++ Seq(
      scalaSource := baseDirectory.value / "src" / "test" / "unit",
      resourceDirectory := baseDirectory.value / "src" / "test" / "unit" / "resources",
    ))

  lazy val unitTests: Project => Project = _.settings(unitTestSettings)

}
