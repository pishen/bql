package bql

import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.QueryParameterValue
import com.google.cloud.bigquery.StandardSQLTypeName

import java.lang
import scala.jdk.CollectionConverters._

case class BQL(statement: String, params: Seq[QueryParameterValue]) {
  def queryConfigBuilder =
    QueryJobConfiguration
      .newBuilder(statement)
      .setPositionalParameters(params.asJava)
  def queryConfig = queryConfigBuilder.build()
}

object BQL {
  def create(sc: StringContext, args: Any*) = {
    val parts = sc.parts
    val stmt = args.zip(parts.tail).foldLeft(parts.head) {
      case (stmt, (bql: BQL, part)) =>
        stmt + bql.statement + part
      case (stmt, (_, part)) =>
        stmt + "?" + part
    }
    val params = args.flatMap {
      case bql: BQL   => bql.params
      case v: Boolean => Seq(QueryParameterValue.bool(v))
      case v: String  => Seq(QueryParameterValue.string(v))
      case v: Int     => Seq(QueryParameterValue.int64(v.toLong))
      case v: Long    => Seq(QueryParameterValue.int64(v))
      case v: Double  => Seq(QueryParameterValue.float64(v))
      case v: Float   => Seq(QueryParameterValue.float64(v.toDouble))
      case s @ Seq(_: Boolean, _*) =>
        val arr = s.asInstanceOf[Seq[Boolean]].map(lang.Boolean.valueOf).toArray
        Seq(QueryParameterValue.array(arr, StandardSQLTypeName.BOOL))
      case s @ Seq(_: String, _*) =>
        val arr = s.asInstanceOf[Seq[String]].toArray
        Seq(QueryParameterValue.array(arr, StandardSQLTypeName.STRING))
      case s @ Seq(_: Int, _*) =>
        val arr = s.asInstanceOf[Seq[Int]].map(lang.Integer.valueOf).toArray
        Seq(QueryParameterValue.array(arr, StandardSQLTypeName.INT64))
      case s @ Seq(_: Long, _*) =>
        val arr = s.asInstanceOf[Seq[Long]].map(lang.Long.valueOf).toArray
        Seq(QueryParameterValue.array(arr, StandardSQLTypeName.INT64))
      case s @ Seq(_: Double, _*) =>
        val arr = s.asInstanceOf[Seq[Double]].map(lang.Double.valueOf).toArray
        Seq(QueryParameterValue.array(arr, StandardSQLTypeName.FLOAT64))
      case s @ Seq(_: Float, _*) =>
        val arr = s.asInstanceOf[Seq[Float]].map(lang.Float.valueOf).toArray
        Seq(QueryParameterValue.array(arr, StandardSQLTypeName.FLOAT64))
      case _ => sys.error("Unsupported parameter type in bql.")
    }
    BQL(stmt.stripMargin, params)
  }
}
