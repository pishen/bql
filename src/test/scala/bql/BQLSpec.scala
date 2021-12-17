package bql

import com.google.cloud.bigquery.QueryJobConfiguration
import com.google.cloud.bigquery.QueryParameterValue
import com.google.cloud.bigquery.StandardSQLTypeName
import org.scalatest.flatspec.AnyFlatSpec

class BQLSpec extends AnyFlatSpec {
  "BQL" should "build a query config using string interpolation" in {
    val gender = "M"
    val states = Seq("WA", "WI", "WV", "WY")
    val query = """SELECT name, sum(number) as count
      |FROM `bigquery-public-data.usa_names.usa_1910_2013`
      |WHERE gender = ?
      |AND state IN UNNEST(?)
      |GROUP BY name
      |ORDER BY count DESC
      |LIMIT 10""".stripMargin
    val queryConfig = QueryJobConfiguration
      .newBuilder(query)
      .addPositionalParameter(QueryParameterValue.string(gender))
      .addPositionalParameter(
        QueryParameterValue.array(states.toArray, StandardSQLTypeName.STRING)
      )
      .build()
    val bql = bql"""SELECT name, sum(number) as count
      |FROM `bigquery-public-data.usa_names.usa_1910_2013`
      |WHERE gender = ${gender}
      |AND state IN UNNEST(${states})
      |GROUP BY name
      |ORDER BY count DESC
      |LIMIT 10"""
    assert(queryConfig == bql.queryConfig)
  }
}
