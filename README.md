# Movie Rec

A simple Demo of a Movie Recommendation System for Big Data. Scalable development using Spark ML (Machine Learning), Cassandra and Akka technologies.

![](https://raw.githubusercontent.com/edersoncorbari/movie-rec/master/doc/img/movie-rec.png)

## Synopsis

This is a project developed for studies. Using **Machine Learning**, applying the Spark ML **Collaborative Filtering** model. The system consists of an Api Rest, with two endpoints. The first endpoint trains the model, the second endpoint returns a list of movie recommendations to a user using their UUID.

### Architecture

The project architecture uses Akka, Spark and Cassandra, these components can work in a distributed way.

<img src="https://raw.githubusercontent.com/edersoncorbari/movie-rec/master/doc/img/movie-rec-diagram.png" width="800" height="300">
