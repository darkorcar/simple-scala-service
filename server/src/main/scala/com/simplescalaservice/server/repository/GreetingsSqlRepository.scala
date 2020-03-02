package com.simplescalaservice.server.repository

import cats.syntax.all._
import cats.effect._

import doobie._
import doobie.implicits._
import doobie.refined.implicits._
import doobie.Fragments.whereAndOpt

import java.time.Instant
import java.util.UUID
import com.simplescalaservice.model._

class GreetingsSqlRepository[F[_]: Sync](transactor: Transactor[F])
    extends GreetingsRepository[F]
    with UUIDSupport {

  override def list(
      page: PageQuery,
      olderThan: Option[Instant] = None,
      laterThan: Option[Instant] = None
  ): F[Page[Greeting]] = {

    val olderFr = olderThan.map(d => fr"created_timestamp <= $d")
    val laterFr = laterThan.map(d => fr"created_timestamp >= $d")
    val pageFr =
      fr"LIMIT ${page.pageSize} OFFSET ${(page.pageNumber - 1) * page.pageSize} "

    val filterFr = whereAndOpt(olderFr, laterFr)
    val orderFr = fr"ORDER BY created_timestamp ASC"
    val queryFr = fr"""
      SELECT id, greet, created_timestamp FROM greeting
""" ++ filterFr ++ orderFr ++ pageFr

    val countFr = fr"SELECT count(*) FROM greeting" ++ filterFr

    (queryFr.query[Greeting].to[Vector], countFr.query[Long].unique)
      .mapN { (rows, totalCount) =>
        Page(rows, (totalCount / page.pageSize.floatValue).ceil.intValue())
      }
      .transact(transactor)

  }

  override def create(greeting: Greeting): F[Unit] = {
    import greeting._
    sql"""
      INSERT INTO greeting(id, greet, created_timestamp)
            VALUES($id, $greet, $createdTimestamp)
     """.update.run.void
      .transact(transactor)
  }

  def getById(id: UUID): F[Option[Greeting]] = {
    sql"""
      SELECT id, greet, created_timestamp FROM greet where id = $id
      """
      .query[Greeting]
      .option
      .transact(transactor)
  }

}
