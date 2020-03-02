package com.simplescalaservice.server

import cats.effect.{ContextShift, IO, Resource}

import com.typesafe.scalalogging.LazyLogging
import doobie._
import doobie.hikari._

import org.mariadb.jdbc.MariaDbDataSource

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext

final case class DatabaseConfig(
    user: String,
    password: String,
    jdbcUrl: String,
    poolName: String,
    connectionTimeout: FiniteDuration,
    driverClassName: String,
    connectionTestQuery: String,
    maxPoolSize: Int
)

object Database extends LazyLogging {

  def connectionPool(config: DatabaseConfig)(blockingExecutionContext: ExecutionContext)(
      implicit sf: ContextShift[IO]): Resource[IO, HikariTransactor[IO]] = {
    import config._
    for {
      cs <- ExecutionContexts.fixedThreadPool[IO](config.maxPoolSize)
      xa <- HikariTransactor
        .newHikariTransactor[IO](driverClassName, jdbcUrl, user, password, cs, blockingExecutionContext)
      _ <- Resource.liftF(xa.configure { config =>
        IO {
          config.setConnectionTestQuery(connectionTestQuery)
          config.setConnectionTimeout(connectionTimeout.toMillis)
          config.setConnectionInitSql(connectionTestQuery)
          config.setInitializationFailTimeout(connectionTimeout.toMillis)
          config.setPoolName(poolName)
          config.setMaximumPoolSize(maxPoolSize)

          config.addDataSourceProperty("connectTimeout", connectionTimeout.toMillis)
        }
      })
      _ = logger.info(s"Started connection pool $poolName. Maximum size $maxPoolSize")
    } yield xa

  }

  def dataSource(config: DatabaseConfig)(blockingExecutionContext: ExecutionContext)(
      implicit sc: ContextShift[IO]) =
    for {
      cs <- ExecutionContexts.fixedThreadPool[IO](config.maxPoolSize)
      ds <- Resource.liftF(IO {
        val ds = new MariaDbDataSource(config.jdbcUrl)

        ds.setUser(config.user)
        ds.setPassword(config.password)
        ds
      })
      xa = Transactor.fromDataSource[IO][MariaDbDataSource](ds, cs, blockingExecutionContext)
    } yield xa

}
