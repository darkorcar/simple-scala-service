package com.simplescalaservice.server.repository

import java.time.Instant

import com.simplescalaservice.model._
import java.{util => ju}

final case class PageQuery(pageNumber: Int, pageSize: Int)

final case class Page[T](rows: Vector[T], totalPages: Int)

trait GreetingsRepository[F[_]] {

  def list(
      page: PageQuery,
      olderThan: Option[Instant],
      laterThan: Option[Instant]
  ): F[Page[Greeting]]

  def create(greeting: Greeting): F[Unit]

  def getById(id: ju.UUID): F[Option[Greeting]]

}
