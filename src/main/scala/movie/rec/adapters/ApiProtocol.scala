package movie.rec.adapters

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import movie.rec.interceptors.RestResponse
import movie.rec.domain.MovieRecommendation

trait ApiProtocol extends DefaultJsonProtocol with SprayJsonSupport with RestResponse {
  implicit def recommendationFormat: RootJsonFormat[MovieRecommendation.Recommendation] =
    jsonFormat2(MovieRecommendation.Recommendation)

  implicit def recommendationsFormat: RootJsonFormat[MovieRecommendation.Recommendations] =
    jsonFormat1(MovieRecommendation.Recommendations)

  implicit def errorResponseFormat: RootJsonFormat[ErrorResponse] =
    jsonFormat1(ErrorResponse)

  implicit def genericResponseFormat: RootJsonFormat[GenericResponse] =
    jsonFormat1(GenericResponse)
}

