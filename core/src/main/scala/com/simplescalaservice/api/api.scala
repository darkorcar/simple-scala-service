package com.simplescalaservice.api

import tapir._
import tapir.json.circe._

import com.simplescalaservice.model._
import tapir.MediaType.TextPlain

import tapir.model.StatusCodes

import java.time.Instant
import java.time.format.DateTimeFormatter
import scala.util.{Failure, Success, Try}

final case class GreetInput(pageQuery: PageQuery, olderThan: Option[Instant], laterThan: Option[Instant])

object Api extends ApiCodecs {

  private val baseEndpoint = endpoint
    .in("api" / "v1")
    .errorOut(jsonBody[ErrorInfo])

  val list: Endpoint[GreetInput, ErrorInfo, Page[Greeting], Nothing] = baseEndpoint.get
    .in("greetings")
    .in(pageQueryInput)
    .in(query[Option[Instant]]("older-than"))
    .in(query[Option[Instant]]("later-than"))
    .mapInTo(GreetInput)
    .out(jsonBody[Page[Greeting]])

  val create: Endpoint[CreateGreet, ErrorInfo, Unit, Nothing] = baseEndpoint.post
    .in("greetings")
    .in(jsonBody[CreateGreet])
    .out(statusCode(StatusCodes.Created))

}

private[api] trait ApiCodecs {

  val pageQueryInput =
    query[Option[Int]]("page-number")
      .and(query[Option[Int]]("page-size"))
      .map {
        case (pageNumber: Option[Int], pageSize: Option[Int]) =>
          PageQuery(pageNumber.getOrElse(0), pageSize.getOrElse(10))
      }(paging => (Some(paging.number), Some(paging.size)))

  private def decodeGreet(s: String) =
    Greet
      .from(s)
      .fold(e => DecodeResult.Error(s, new RuntimeException(e)), DecodeResult.Value.apply _)

  implicit lazy val codecForGreet: Codec[Greet, TextPlain, String] =
    Codec.stringPlainCodecUtf8.mapDecode(decodeGreet _)(_.value)

  implicit val schemaForGreet: SchemaFor[Greet] = SchemaFor(Schema.SString)

  private val instantFormat = DateTimeFormatter.ISO_INSTANT
  private def decodeInstant(s: String) = Try(instantFormat.parse(s)) match {
    case Success(v)  => DecodeResult.Value(Instant.from(v))
    case Failure(ex) => DecodeResult.Error(s, ex)
  }

  implicit val codecForInstant: Codec[Instant, TextPlain, String] =
    Codec.stringPlainCodecUtf8.mapDecode(decodeInstant _)(instantFormat.format _)
}
