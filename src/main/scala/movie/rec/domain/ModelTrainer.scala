package movie.rec.domain

import akka.actor.Actor
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.ALS
import movie.rec.adapters.repository.CassandraService

class ModelTrainer(sc: SparkContext) extends Actor with Config {

  import ModelTrainerFactory._

  def receive: PartialFunction[Any, Unit] = {
    case Train => trainModel()
  }

  private def trainModel(): Unit = {
    val modelTrain = ALS.train(
      new CassandraService(sc).getMovieDataWithRating,
      model.rank,
      model.iterations,
      model.lambda)

    sender ! TrainingResult(modelTrain)
    context.stop(this.self)
  }
}
