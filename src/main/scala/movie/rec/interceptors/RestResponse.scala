package movie.rec.interceptors

trait RestResponse {
  case class GenericResponse(msg: String) extends Exception(msg)

  case class ErrorResponse(msg: String) extends Exception(msg)
}

