package bql

import com.google.cloud.bigquery.QueryParameterValue
import com.google.cloud.bigquery.StandardSQLTypeName

import java.lang

trait Converter[T] {
  def single(t: T): QueryParameterValue
  def seq(ts: Seq[T]): QueryParameterValue
}

object Converter {
  import QueryParameterValue._
  import StandardSQLTypeName._

  def create[T](
      f1: T => QueryParameterValue,
      f2: Seq[T] => QueryParameterValue
  ) = new Converter[T] {
    def single(t: T) = f1(t)
    def seq(ts: Seq[T]) = f2(ts)
  }

  implicit val boolConverter = create[Boolean](
    t => bool(t),
    ts => array(ts.map(lang.Boolean.valueOf).toArray, BOOL)
  )
  implicit val stringConverter = create[String](
    t => string(t),
    ts => array(ts.toArray, STRING)
  )
  implicit val intConverter = create[Int](
    t => int64(t.toLong),
    ts => array(ts.map(lang.Integer.valueOf).toArray, INT64)
  )
  implicit val longConverter = create[Long](
    t => int64(t),
    ts => array(ts.map(lang.Long.valueOf).toArray, INT64)
  )
  implicit val doubleConverter = create[Double](
    t => float64(t),
    ts => array(ts.map(lang.Double.valueOf).toArray, FLOAT64)
  )
  implicit val floatConverter = create[Float](
    t => float64(t.toDouble),
    ts => array(ts.map(lang.Float.valueOf).toArray, FLOAT64)
  )
}
