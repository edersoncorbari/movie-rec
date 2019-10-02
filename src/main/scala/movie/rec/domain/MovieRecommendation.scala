package movie.rec.domain

import akka.actor.Actor
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.joda.time.DateTime
import movie.rec.adapters.repository.CassandraService

class MovieRecommendation(sc: SparkContext) extends Actor with Config {

  import MovieFactory._

  var modelMatrix: Option[MatrixFactorizationModel] = None

  def receive: PartialFunction[Any, Unit] = {
    case Train => trainModel()
    case GenerateRecommendations(userId) => generateRecommendations(userId, model.iterations)
    case ModelTrainerFactory.TrainingResult(model) => storeModel(model)
  }

  private def trainModel(): Unit = {
    val trainer = context.actorOf(ModelTrainerFactory.props(sc), http.name)
    trainer ! ModelTrainerFactory.Train
  }

  private def storeModel(model: MatrixFactorizationModel): Unit = this.modelMatrix = Some(model)

  private def generateRecommendations(userId: Int, count: Int): Unit = {

    val dateTime = DateTime.now()

    val results = this.modelMatrix match {
      case Some(m) => m.recommendProducts(userId, count)
        .map(rating => RecommendationItem(userId, rating.product, rating.rating, dateTime.toString()))
        .toList

      case None => Nil
    }

    val cassandraService = new CassandraService(sc)
    cassandraService.saveModelResult(results)

    sender ! RecommendationItemWithNames(
      cassandraService.joinResultWithMovie(userId, dateTime).collect().toList)
  }
}
