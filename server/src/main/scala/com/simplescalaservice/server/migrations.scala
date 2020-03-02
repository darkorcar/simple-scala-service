package com.simplescalaservice.server

import cats.effect.IO

import javax.sql.DataSource
import doobie._

import org.flywaydb.core.Flyway

object Migrations {
  def migrate[D <: DataSource](xa: Transactor.Aux[IO, D]): IO[Unit] =
    withFlyway(xa) { fl =>
      fl.migrate(); ()
    }.uncancelable

  def validate[D <: DataSource](xa: Transactor.Aux[IO, D]): IO[Unit] =
    withFlyway(xa) { fl =>
      fl.validate()
    }

  def clean[D <: DataSource](xa: Transactor.Aux[IO, D]): IO[Unit] =
    withFlyway(xa) { fl =>
      fl.clean()
    }.uncancelable

  private def withFlyway[D <: DataSource](xa: Transactor.Aux[IO, D])(f: Flyway => Unit) =
    xa.configure(ds =>
      IO {
        val flyway = Flyway
          .configure()
          .dataSource(ds)
          .load()
        f(flyway)
    })

}
