package com.simplescalaservice.api

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

final case class PageQuery(number: Int, size: Int)

final case class Page[T](elements: Vector[T], pageNumber: Int, pageSize: Int, totalPages: Int)

object Page {
  implicit def encoder[T: Encoder]: Encoder[Page[T]] = deriveEncoder
  implicit def decoder[T: Decoder]: Decoder[Page[T]] = deriveDecoder

}
