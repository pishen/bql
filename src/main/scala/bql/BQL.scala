package bql

import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.QueryParameterValue

import scala.jdk.CollectionConverters._

case class BQL(statement: String, values: Seq[QueryParameterValue]) {
  def queryConfigBuilder =
    QueryJobConfiguration
      .newBuilder(statement)
      .setPositionalParameters(values.asJava)
  def queryConfig = queryConfigBuilder.build()
}

object BQL {
  def create(sc: StringContext, params: Param*) = {
    val parts = sc.parts
    val stmt = params.zip(parts.tail).foldLeft(parts.head) {
      case (stmt, (BQLParam(bql), part)) =>
        stmt + bql.statement + part
      case (stmt, (_, part)) =>
        stmt + "?" + part
    }
    val values = params.flatMap {
      case BQLParam(bql)     => bql.values
      case ValueParam(value) => Seq(value)
    }
    BQL(stmt.stripMargin, values)
  }
}
