# bql

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.pishen/bql_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.pishen/bql_2.13)
[![javadoc](https://javadoc.io/badge2/net.pishen/bql_2.13/javadoc.svg)](https://javadoc.io/doc/net.pishen/bql_2.13)

Build BigQuery SQL with Scala String interpolation, which supports [BigQuery parameterized queries](https://cloud.google.com/bigquery/docs/parameterized-queries) and prevent SQL injection automatically.

## Installation

```scala
libraryDependencies += "net.pishen" %% "bql" % "<version>"
```

## Usage

```scala
import bql._

val bigquery = BigQueryOptions.getDefaultInstance().getService()

val gender = "M"
val states = Seq("WA", "WI", "WV", "WY")

val bql = bql"""SELECT name, sum(number) as count
  |FROM `bigquery-public-data.usa_names.usa_1910_2013`
  |WHERE gender = ${gender}
  |AND state IN UNNEST(${states})
  |GROUP BY name
  |ORDER BY count DESC
  |LIMIT 10"""

bigquery.query(bql.queryConfig)
```
