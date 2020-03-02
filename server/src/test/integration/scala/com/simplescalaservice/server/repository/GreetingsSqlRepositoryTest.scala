package com.simplescalaservice.server.repository

import cats.syntax.all._
import cats.instances.list._
import cats.effect._
import org.scalatest._

import doobie._
import org.mariadb.jdbc.MariaDbDataSource

import com.simplescalaservice.model._

import java.util.UUID
import scala.concurrent.ExecutionContext
import java.time.LocalDateTime

import java.time.temporal.ChronoUnit
import java.time.ZoneOffset

class GreetingsSqlRepositoryTest extends WordSpec with Matchers with OptionValues {

  implicit val cs = IO.contextShift(ExecutionContext.global)
  import Schema._

  "GreetingsSqlRepository" should {

    "Get empty list when there are no greetings" in {
      val result = schemaResource(xa)
        .use { _ =>
          repository.list(firstPage)
        }
        .unsafeRunSync()

      (result.rows should have).size(0)
      result.totalPages shouldBe 0

    }

    "Create Greeting" in {
      val result = schemaResource(xa)
        .use { _ =>
          repository.create(greeting1) *>
            repository.getById(greeting1.id)
        }
        .unsafeRunSync()

      result.value.id shouldBe greeting1.id
      result.value.greet shouldBe greeting1.greet
    }

    "list greetings with no filters" in {

      val result = schemaResource(xa)
        .use { _ =>
          List(greeting1, greeting2).traverse(repository.create _) *>
            repository.list(firstPage)
        }
        .unsafeRunSync()

      result.totalPages shouldBe 1
      (result.rows should have).size(2)
    }

    "Filter listing by older than" in {
      val (result1, result2) = schemaResource(xa)
        .use { _ =>
          List(greeting1, greeting2).traverse(repository.create _) *>
            (
              repository.list(firstPage, olderThan = Some(fourMonthsLater.toInstant((ZoneOffset.UTC)))),
              repository.list(firstPage, olderThan = Some(creationDate.toInstant((ZoneOffset.UTC))))).tupled
        }
        .unsafeRunSync()

      (result1.rows should have).size(2)
      result2.totalPages shouldBe 0
      (result2.rows should have).size(0)
    }

    "Filter listing by later than" in {
      val (result1, result2) = schemaResource(xa)
        .use { _ =>
          List(greeting1, greeting2).traverse(repository.create _) *>
            (
              repository.list(firstPage, laterThan = Some(fourMonthsLater.toInstant(ZoneOffset.UTC))),
              repository.list(firstPage, laterThan = Some(creationDate.toInstant((ZoneOffset.UTC))))).tupled
        }
        .unsafeRunSync()

      result1.totalPages shouldBe 0
      (result1.rows should have).size(0)

      (result2.rows should have).size(2)
    }

    "Compute total pages properly" in {
      val result = schemaResource(xa)
        .use { _ =>
          List(greeting1, greeting2).traverse(repository.create _) *>
            repository.list(PageQuery(1, 1))
        }
        .unsafeRunSync()

      (result.rows should have).size(1)
      result.totalPages shouldBe 2
    }
  }

  val creationDate = LocalDateTime.now()
  val threeMonthsLater = creationDate.plus(3, ChronoUnit.MONTHS)
  val fourMonthsLater = creationDate.plus(4, ChronoUnit.MONTHS)
  val oneDaySooner = creationDate.minus(1, ChronoUnit.DAYS)

  val greeting1 = Greeting(
    UUID.randomUUID(),
    "Good morning",
    creationDate.toInstant(ZoneOffset.UTC))

  val greeting2 = Greeting(
    UUID.randomUUID(),
    "Good evening",
    creationDate.toInstant(ZoneOffset.UTC))

  lazy val dataSource = {
    val ds = new MariaDbDataSource("")
    ds.setUser("mariadbUser")
    ds.setPassword("mariadbPassword")
    ds
  }

  lazy val xa = Transactor.fromDataSource[IO](dataSource, ExecutionContext.global, ExecutionContext.global)

  lazy val repository = new GreetingsSqlRepository(xa)

  val firstPage = PageQuery(1, 100)
}
