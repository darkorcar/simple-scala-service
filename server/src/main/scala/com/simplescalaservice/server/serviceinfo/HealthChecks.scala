package com.simplescalaservice.server.serviceinfo

import cats.effect._

import doobie.hikari.HikariTransactor
import eu.timepit.refined.auto._
import sup._
import sup.modules.doobie._
import com.simplescalaservice.server.Resources

import scala.concurrent.duration._

object HealthChecks {

  def db(
      xa: HikariTransactor[IO]
  )(implicit cs: ContextShift[IO], t: Timer[IO]) = {
    // I believe the connectionCheck timeout is for if the connection to a working
    // db takes more than that time to respond then it will be sick.
    // The timeoutToFailure is for actually when the IO itself hangs and doesn't
    // reply in that timeout which occurs when the database itself is down.
    // In any of the cases if the IO is failed it says it is Sick.

    connectionCheck(xa)(timeoutSeconds = Some(2))
      .through(mods.timeoutToFailure(3.seconds))
      .through(mods.recoverToSick)
      .through(mods.tagWith("db-"))
  }

  def report(
      resources: Resources
  )(implicit cs: ContextShift[IO], t: Timer[IO]) = {
    db(resources.transactor).check
  }

}
