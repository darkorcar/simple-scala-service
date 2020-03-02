package com.simplescalaservice.model

import java.util.UUID
import java.time.Instant
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import io.circe.refined._

case class Greeting(
    id: UUID,
    greet: String,
    createdTimestamp: Instant
)

object Greeting {
  implicit val encoder: Encoder[Greeting] = deriveEncoder
  implicit val decoder: Decoder[Greeting] = deriveDecoder
}

case class CreateGreet(greet: Greet)

object CreateGreet {
  implicit val encoder: Encoder[CreateGreet] = deriveEncoder
  implicit val decoder: Decoder[CreateGreet] = deriveDecoder
}
