package movie.rec.domain

import akka.actor.Props
import org.apache.spark.SparkContext

object MovieFactory {

  case object Train {}
  case class GenerateRecommendations(userId: Int)

  case class RecommendationItem(userId: Int, itemId: Int, rating: Double, datetime: String)

  case class RecommendationItemWithName(userId: Int, movieId: Int, name: String, rating: Double, datetime: String)

  case class RecommendationItemWithNames(items: Seq[RecommendationItemWithName])

  def props(sc: SparkContext): Props = Props(new MovieRecommendation(sc))
}

object ModelTrainerFactory {

  import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

  case object Train
  case class TrainingResult(model: MatrixFactorizationModel)

  def props(sc: SparkContext): Props = Props(new ModelTrainer(sc))
}
