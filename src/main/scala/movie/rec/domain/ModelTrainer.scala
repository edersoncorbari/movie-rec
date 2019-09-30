package movie.rec.domain

import akka.actor.{Props, Actor}
import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.{Rating, ALS, MatrixFactorizationModel}
import com.datastax.spark.connector._

object ModelTrainer {

  case object Train
  case class TrainingResult(model: MatrixFactorizationModel)

  def props(sc: SparkContext): Props = Props(new ModelTrainer(sc))
}

class ModelTrainer(sc: SparkContext) extends Actor with Config {

  import ModelTrainer._

  def receive: PartialFunction[Any, Unit] = {
    case Train => trainModel()
  }

  private def trainModel(): Unit = {

    val ratings = sc.cassandraTable(
      cassandra.keyspace, cassandra.table).map(record => Rating(
      record.get[Int]("user_id"),
      record.get[Int]("item_id"),
      record.get[Double]("rating")))

    // @TODO: Test data.
    ratings.repartition(1).saveAsTextFile("test.txt")

    val modelTrain = ALS.train(ratings, model.rank, model.iterations, model.lambda)
    sender ! TrainingResult(modelTrain)

    context.stop(this.self)
  }
}
