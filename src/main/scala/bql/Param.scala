package bql

import com.google.cloud.bigquery.QueryParameterValue

import scala.language.implicitConversions

sealed trait Param
case class ValueParam(value: QueryParameterValue) extends Param
case class BQLParam(bql: BQL) extends Param

object Param {
  implicit def convert[T](t: T)(implicit conv: Converter[T]): Param =
    ValueParam(conv.single(t))
  implicit def convert[T](ts: Seq[T])(implicit conv: Converter[T]): Param =
    ValueParam(conv.seq(ts))
  implicit def convert(bql: BQL): Param = BQLParam(bql)
}
