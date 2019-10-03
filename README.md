# Movie Rec

A simple Demo of a Movie Recommendation System for Big Data. Scalable development using Spark ML (Machine Learning), Cassandra and Akka technologies.

<p align="center"> 
<img src="https://raw.githubusercontent.com/edersoncorbari/movie-rec/master/doc/img/movie-rec.png">
</p>

## Synopsis

This is a project developed for studies. Using **Machine Learning**, applying the Spark ML **Collaborative Filtering** model. The system consists of an Api Rest, with two endpoints. The first endpoint trains the model, the second endpoint returns a list of movie recommendations to a user using their UUID.

More detailed information can be found from the sites below:

 * [https://edersoncorbari.github.io/tutorials/](https://edersoncorbari.github.io/tutorials/)
 
### Architecture

The project architecture uses Akka, Spark and Cassandra, these components can work in a distributed way.

<p align="center"> 
<img src="https://raw.githubusercontent.com/edersoncorbari/movie-rec/master/doc/img/movie-rec-diagram.png" width="800" height="300">
</p>

### Quick start

You need to install SBT on your machine and create a docker for Cassandra.

#### 1. Get the code

Now run the commands below to compile the project:

```shell
$ git clone https://github.com/edersoncorbari/movie-rec.git
$ cd movie-rec
```

#### 2. Docking and Configuring Cassandra

The Cassandra version used was 3.11.4, possibly works on any version up.

```shell
$ docker pull cassandra:3.11.4
```

Creating and running the docker. The application is ready to use these settings.

```shell
docker run --name cassandra-movie-rec -p 127.0.0.1:9042:9042 -p 127.0.0.1:9160:9160 -d cassandra:3.11.4
```

Make sure it's up and running.

```shell
$ docker ps | grep cassandra
```

In the *project directory* there are the datasets already prepared to put in Cassandra.

```shell
$ cat dataset/ml-100k.tar.gz | docker exec -i cassandra-movie-rec tar zxvf - -C /tmp
```

Creating the schema and loading the datasets:

```shell
$ docker exec -it cassandra-movie-rec cqlsh -f /tmp/ml-100k/schema.cql
```

#### 3. Data Model and Verifying Data

The data in Cassandra is modeled as follows:

<p align="center"> 
<img src="https://raw.githubusercontent.com/edersoncorbari/movie-rec/master/doc/img/cassandra-data-models.png">
</p>

Enter the Cassandra console using CQLSH and verify the data:

```shell
$ docker exec -it cassandra-movie-rec cqlsh
```
