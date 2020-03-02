package com.simplescalaservice.server.repository

import cats.data.NonEmptyList

import doobie._, doobie.enum.JdbcType

import java.util.UUID

private[repository] trait UUIDSupport {

  implicit val meta: Meta[UUID] = Meta.Advanced.one[UUID](
    JdbcType.Binary,
    NonEmptyList.of("BINARY(16)"),
    (rs, index) => fromBytes(rs.getBytes(index)),
    (ps, index, v) => ps.setBytes(index, toBytes(v)),
    (ps, index, v) => ps.updateBytes(index, toBytes(v))
  )

  // basically copied from slick
  def toBytes(uuid: UUID) =
    if (uuid eq null) null
    else {
      val msb = uuid.getMostSignificantBits
      val lsb = uuid.getLeastSignificantBits
      val buff = new Array[Byte](16)
      for (i <- 0 until 8) {
        buff(i) = ((msb >> (8 * (7 - i))) & 255).toByte
        buff(8 + i) = ((lsb >> (8 * (7 - i))) & 255).toByte
      }
      buff
    }
  def fromBytes(data: Array[Byte]) =
    if (data eq null) null
    else {
      var msb = 0L
      var lsb = 0L
      for (i <- 0 until 8) {
        msb = (msb << 8) | (data(i) & 0xff)
      }
      for (i <- 8 until 16) {
        lsb = (lsb << 8) | (data(i) & 0xff)
      }
      new UUID(msb, lsb)
    }
}
