package movie.rec.adapters

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout
import akka.pattern.ask
import org.apache.spark.{SparkConf, SparkContext}
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.concurrent.duration._
import movie.rec.interceptors.RestResponse
import movie.rec.domain.MovieRecommendation
import movie.rec.domain.Config

object ConfigParam extends Config {
  val server: String = http.server
  val port: Int = http.port
  val name: String = http.name
  val master: String = spark.master
  val repository: String = cassandra.server
}

class ApiService(server: String = ConfigParam.server, port: Int = ConfigParam.port)(implicit val system: ActorSystem)
  extends ApiProtocol with RestResponse {

  implicit val materializer: Materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val timeout: Timeout = 30 seconds

  lazy val config = new SparkConf()
  config.setMaster(ConfigParam.master)
  config.setAppName(ConfigParam.name)
  config.set("spark.cassandra.connection.host", ConfigParam.repository)

  lazy val sparkContext = new SparkContext(config)
  lazy val movieRecommendation = system.actorOf(MovieRecommendation.props(sparkContext))

  val errorHandler = ExceptionHandler {
    case e: Exception => complete {
      (StatusCodes.InternalServerError -> ErrorResponse("Internal server error."))
    }
  }

  val route = {
    handleExceptions(errorHandler) {
      pathPrefix("movie-get-recommendation") {
        path(Segment) { id =>
          get {
            complete {
              (movieRecommendation ? MovieRecommendation.GenerateRecommendations(id.toInt))
                .mapTo[MovieRecommendation.Recommendations]
                .flatMap(result => Future {
                  (StatusCodes.OK -> result)
                })
            }
          }
        }
      } ~ path("movie-model-train") {
        post {
          movieRecommendation ! MovieRecommendation.Train

          complete {
            (StatusCodes.OK -> GenericResponse("Training started..."))
          }
        }
      }
    }
  }

  def start(): Unit = Http().bindAndHandle(route, server, port)
}
