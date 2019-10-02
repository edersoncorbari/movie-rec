package movie.rec.adapters.http

import akka.actor.ActorSystem
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
import movie.rec.domain._
import movie.rec.domain.Config

class ApiService(server: String, port: Int)(implicit val system: ActorSystem)
  extends ApiProtocol with RestResponse with Config {

  implicit val materializer: Materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val timeout: Timeout = 30 seconds

  lazy val config = new SparkConf()
  config.setMaster(spark.master)
  config.setAppName(http.name)
  config.set("spark.cassandra.connection.host", cassandra.server)

  lazy val sparkContext = new SparkContext(config)
  lazy val movieRecommendation = system.actorOf(MovieFactory.props(sparkContext))

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
              (movieRecommendation ? MovieFactory.GenerateRecommendations(id.toInt))
                .mapTo[MovieFactory.RecommendationItemWithNames]
                .flatMap(result => Future {
                  (StatusCodes.OK -> result)
                })
            }
          }
        }
      } ~ path("movie-model-train") {
        post {
          movieRecommendation ! MovieFactory.Train

          complete {
            (StatusCodes.OK -> GenericResponse("Training started..."))
          }
        }
      }
    }
  }

  def start(): Unit = akka.http.scaladsl.Http().bindAndHandle(route, server, port)
}
