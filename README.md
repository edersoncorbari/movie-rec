# Movie Rec

A simple Demo of a **Movie Recommendation System** for Big Data. Scalable development using Spark ML (**Machine Learning**), Cassandra and Akka technologies.

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

#### 3. Data Model

The keyspace is called *movies*. The data in Cassandra is modeled as follows:

<p align="center"> 
<img src="https://raw.githubusercontent.com/edersoncorbari/movie-rec/master/doc/img/cassandra-data-models.png">
</p>

Organization:

| Collection | Comments |
| ------ | ------ | 
| *movies.uitem* | Contains available movies, total dataset used is 1682. |
| *movies.udata* | Contains movies rated by each user, total dataset used is 100000.| 
| *movies.uresult* | Where the data calculated by the model is saved, by default it is empty. |

#### 4. Verifying the data

Enter the Cassandra console using *CQLSH* and verify the data:

```shell
$ docker exec -it cassandra-movie-rec cqlsh
```

The syntax is similar to our old known SQL:

```sql
cqlsh> use movies;
cqlsh:movies> select count(1) from uitems; -- Must be: 1682
cqlsh:movies> select count(1) from udata;  -- Must be: 100000
cqlsh:movies> describe uresult;
```

#### 5. Running the Project

It is important before setting the Spark variable:

```shell
$ export SPARK_LOCAL_IP="127.0.0.1"
```

Enter the project root folder and run the commands, if this is the first time SBT will download the necessary dependencies.

```shell
$ sbt compile test run
```

Rock and roll! The Akka Http is running with Spark. 

> Note: You can use the *curl* command directly, but jsoncurl makes json's response pretty!

Now! In another terminal run the command to train the model:

```shell
$ curljson -XPOST http://localhost:8080/movie-model-train
```

*The answer should be:*

```json
{
  "msg": "Training started..."
}
```

This will start the model training. You can then run the command to see results with recommendations. Example:

```shell
$ curljson -XGET http://localhost:8080/movie-get-recommendation/1
```

> Note: The number parameter at the end is the uuid of a user, you look for other ids in Cassandra and test.

*The answer should be:*

```json
{
    "items": [
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 390,
            "name": "Fear of a Black Hat (1993)",
            "rating": 6.024536312414079,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 408,
            "name": "Close Shave, A (1995)",
            "rating": 5.477789547740662,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 718,
            "name": "In the Bleak Midwinter (1995)",
            "rating": 5.451157889522041,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 960,
            "name": "Naked (1993)",
            "rating": 5.538010212703824,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 962,
            "name": "Ruby in Paradise (1993)",
            "rating": 5.474417923036245,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 1019,
            "name": "Die xue shuang xiong (Killer, The) (1989)",
            "rating": 6.06760015836501,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 1159,
            "name": "Stalker (1979)",
            "rating": 5.734765600568821,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 1449,
            "name": "Pather Panchali (1955)",
            "rating": 5.467671438168102,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 1463,
            "name": "Boys, Les (1997)",
            "rating": 6.1775396236474664,
            "userId": 1
        },
        {
            "datetime": "Thu Oct 03 11:14:21 BRT 2019",
            "movieId": 1643,
            "name": "Angel Baby (1995)",
            "rating": 5.531300372911295,
            "userId": 1
        }
    ]
}
```

That’s icing on the cake! Remember that the setting is set to show *10* movies recommendations per user.
