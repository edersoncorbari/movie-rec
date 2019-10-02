package movie.rec.adapters.repository

import org.apache.spark.SparkContext
import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.rdd.RDD
import org.joda.time.DateTime
import com.datastax.spark.connector._
import movie.rec.domain.Config

class CassandraService(sc: SparkContext) extends Config {

  import movie.rec.domain.MovieFactory._

  lazy val getMovieDataWithRating: RDD[Rating] = {
    sc.cassandraTable(
      cassandra.keyspace, cassandra.tableData).map(r => Rating(
        r.get[Int]("user_id"),
        r.get[Int]("item_id"),
        r.get[Double]("rating")))
  }

  def joinResultWithMovie(userId: Int, dateTime: DateTime): RDD[RecommendationItemWithName] = {
    val collection = sc.cassandraTable(cassandra.keyspace, cassandra.tableResult)
      .where("datetime = ?", dateTime)
      .joinWithCassandraTable(cassandra.keyspace, cassandra.tableItem)
      .filter(r => r._1.getInt("user_id") == userId)

    collection.map(x => RecommendationItemWithName(
      x._1.get[Int]("user_id"), x._1.get[Int]("movie_id"), x._2.getString("name"),
      x._1.get[Double]("rating"), x._1.getDate("datetime").toString))
  }

  def saveModelResult(results: List[RecommendationItem]): Unit = {
    val collection = sc.parallelize(
      results.map(x => (x.userId, x.itemId, x.rating, x.datetime))).cache()

    collection.saveToCassandra(cassandra.keyspace, cassandra.tableResult,
      SomeColumns("user_id" as "_1", "movie_id" as "_2", "rating" as "_3", "datetime" as "_4"))
  }

}
