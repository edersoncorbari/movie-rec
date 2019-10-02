package movie.rec.adapters.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import movie.rec.interceptors.RestResponse

trait ApiProtocol extends DefaultJsonProtocol with SprayJsonSupport with RestResponse {

  import movie.rec.domain._

  implicit def recommendationItemWithNameFormat: RootJsonFormat[MovieFactory.RecommendationItemWithName] =
    jsonFormat5(MovieFactory.RecommendationItemWithName)

  implicit def recommendationItemWithNamesFormat: RootJsonFormat[MovieFactory.RecommendationItemWithNames] =
    jsonFormat1(MovieFactory.RecommendationItemWithNames)

  implicit def errorResponseFormat: RootJsonFormat[ErrorResponse] =
    jsonFormat1(ErrorResponse)

  implicit def genericResponseFormat: RootJsonFormat[GenericResponse] =
    jsonFormat1(GenericResponse)
}
