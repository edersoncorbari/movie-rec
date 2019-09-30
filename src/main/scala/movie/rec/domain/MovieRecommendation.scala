package movie.rec.domain

import akka.actor.{Props, Actor}
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

object MovieRecommendation {

  case object Train {}
  case class GenerateRecommendations(userId: Int)
  case class Recommendation(contentItemId: Int, rating: Double)
  case class Recommendations(items: Seq[Recommendation])

  def props(sc: SparkContext): Props = Props(new MovieRecommendation(sc))
}

class MovieRecommendation(sc: SparkContext) extends Actor with Config {

  import MovieRecommendation._

  var modelMatrix: Option[MatrixFactorizationModel] = None

  def receive: PartialFunction[Any, Unit] = {
    case Train => trainModel()
    case GenerateRecommendations(userId) => generateRecommendations(userId, model.iterations)
    case ModelTrainer.TrainingResult(model) => storeModel(model)
  }

  private def trainModel(): Unit = {
    val trainer = context.actorOf(ModelTrainer.props(sc), http.name)
    trainer ! ModelTrainer.Train
  }

  private def storeModel(model: MatrixFactorizationModel): Unit = this.modelMatrix = Some(model)

  private def generateRecommendations(userId: Int, count: Int): Unit = {

    val results = this.modelMatrix match {
      case Some(m) => m.recommendProducts(userId, count)
        .map(rating => Recommendation(rating.product, rating.rating))
        .toList

      case None => Nil
    }

    sender ! Recommendations(results)
  }
}
