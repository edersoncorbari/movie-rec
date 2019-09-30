package movie.rec.domain

trait ConfigInterface {

  case class Http(server: String,
                  port: Int,
                  name: String,
                  timeout: Int)

  case class Cassandra(server: String,
                       keyspace: String,
                       table: String)

  case class Model(rank: Int,
                   iterations: Int,
                   lambda: Double)

  case class Spark(master: String)
}

trait Config extends ConfigInterface {

  import com.typesafe.config.ConfigFactory

  private[this] lazy val loadedConfig = ConfigFactory.load()

  lazy val http = Http(
    loadedConfig.getString("http.server"),
    loadedConfig.getInt("http.port"),
    loadedConfig.getString("http.name"),
    loadedConfig.getInt("http.timeout"))

  lazy val cassandra = Cassandra(
    loadedConfig.getString("cassandra.server"),
    loadedConfig.getString("cassandra.keyspace"),
    loadedConfig.getString("cassandra.table"))

  lazy val model = Model(
    loadedConfig.getInt("model.rank"),
    loadedConfig.getInt("model.iterations"),
    loadedConfig.getDouble("model.lambda"))

  lazy val spark = Spark(
    loadedConfig.getString("spark.master"))
}