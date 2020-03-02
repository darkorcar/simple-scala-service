package com.simplescalaservice.server.repository

import cats.effect._
import doobie._

import com.simplescalaservice.server.Migrations

object Schema {

  def schemaResource[D <: javax.sql.DataSource](transactor: Transactor.Aux[IO, D]): Resource[IO, Unit] =
    Resource.make(Migrations.migrate(transactor))(_ => Migrations.clean(transactor))
}
