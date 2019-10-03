package movie.rec.unit.domain

import org.scalatest.{FlatSpec, Matchers}

import movie.rec.domain.Config

class ConfigFactorySpec extends FlatSpec with Matchers with Config {

  it should "validating (types) config factory" in {
    Http.toString() shouldEqual "Http"
    Cassandra.toString() shouldEqual "Cassandra"
    Model.toString() shouldEqual "Model"
    Spark.toString() shouldEqual "Spark"
  }

  it should "validating (http) config directives" in {
    http.server shouldEqual "localhost"
    http.port shouldEqual 8080
    http.name shouldEqual "API-Movie-Recommendation"
    http.timeout shouldEqual 30
  }

  it should "validating (cassandra) config directives" in {
    cassandra.server shouldEqual "127.0.0.1"
    cassandra.keyspace shouldEqual "movies"
    cassandra.tableData shouldEqual "udata"
    cassandra.tableItem shouldEqual "uitem"
    cassandra.tableResult shouldEqual "uresult"
  }

  it should "validating (model) config directives" in {
    model.rank shouldEqual 10
    model.iterations shouldEqual 10
    model.lambda shouldEqual 0.01
  }

  it should "validating (spark) config directives" in {
    spark.master shouldEqual "local[*]"
  }

}
