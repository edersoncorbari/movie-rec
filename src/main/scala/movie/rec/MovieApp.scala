package movie.rec

import akka.actor.ActorSystem
import akka.util.Timeout
import scala.concurrent.duration._
import scala.language.postfixOps
import movie.rec.adapters.http.ApiService
import movie.rec.domain.Config

object MovieApp extends App with Config {

  implicit val system: ActorSystem = ActorSystem(http.name)
  implicit val timeout: Timeout = http.timeout seconds

  new ApiService(http.server, http.port).start()
}
