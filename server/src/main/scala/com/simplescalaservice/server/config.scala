package com.simplescalaservice.server

import cats.effect.IO

import pureconfig._
import pureconfig.generic.ProductHint

final case class Config(
                         database: DatabaseConfig,
                         http: HttpConfig,
)

object Config {
  import pureconfig.generic.auto._

  implicit def hint[T]: ProductHint[T] =
    ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  def load: IO[Config] = IO {
    pureconfig.loadConfigOrThrow[Config]
  }
}
