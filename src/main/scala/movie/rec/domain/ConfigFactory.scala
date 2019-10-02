package movie.rec.domain

trait ConfigFactory {

  case class Http(server: String, port: Int, name: String, timeout: Int)

  case class Cassandra(server: String, keyspace: String,
                       tableData: String, tableItem: String,
                       tableResult: String)

  case class Model(rank: Int, iterations: Int, lambda: Double)

  case class Spark(master: String)
}

trait Config extends ConfigFactory {

  import com.typesafe.config.ConfigFactory

  private[this] lazy val config = ConfigFactory.load()

  lazy val http = Http(
    config.getString("http.server"),
    config.getInt("http.port"),
    config.getString("http.name"),
    config.getInt("http.timeout"))

  lazy val cassandra = Cassandra(
    config.getString("cassandra.server"),
    config.getString("cassandra.keyspace"),
    config.getString("cassandra.tableData"),
    config.getString("cassandra.tableItem"),
    config.getString("cassandra.tableResult"))

  lazy val model = Model(
    config.getInt("model.rank"),
    config.getInt("model.iterations"),
    config.getDouble("model.lambda"))

  lazy val spark = Spark(
    config.getString("spark.master"))
}