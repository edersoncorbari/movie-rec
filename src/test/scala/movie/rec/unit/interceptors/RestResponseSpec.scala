package movie.rec.unit.interceptors

import org.scalatest.{FlatSpec, Matchers}

import movie.rec.interceptors.RestResponse

class RestResponseSpec extends FlatSpec with Matchers with RestResponse {

  it should "validating trap messages used in api rest" in {
    val thrownError = intercept[Exception] {
      throw ErrorResponse("Internal server error.")
    }
    thrownError.getMessage shouldEqual "Internal server error."

    val thrownGeneric = intercept[Exception] {
      throw GenericResponse("Training started...")
    }
    thrownGeneric.getMessage shouldEqual "Training started..."
  }

}
