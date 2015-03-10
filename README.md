# Tepkin

[![Build Status](https://travis-ci.org/fehmicansaglam/tepkin.svg?branch=master)](https://travis-ci.org/fehmicansaglam/tepkin)
![Progress](http://progressed.io/bar/71?title=brewing)

Reactive MongoDB Driver for Scala 2.11 and Java 8 built on top of Akka IO and Akka Streams.

Only MongoDB 3.0+ is supported.

## Contributions
Tepkin is a young but very active project and absolutely needs your help. Good ways to contribute include:

* Raising bugs and feature requests
* Fixing bugs
* Improving the performance
* Adding to the documentation

## Scala examples

### Obtaining a reference to a collection

```scala
val client = MongoClient("mongodb://localhost")
val db = client("tepkin")
val collection = db("mongo_collection_spec")
```

### Insert and update a document

```scala
val document = ("name" := "fehmi") ~ ("surname" := "saglam")

implicit val timeout: Timeout = 5.seconds

val future = for {
  insert <- collection.insert(Seq(document))
  update <- collection.update(
    query = ("name" := "fehmi"),
    update = $set("name" := "fehmi can")
  )
} yield update
```

### Insert and find 100K documents

```scala
implicit val mat = ActorFlowMaterializer()(client.context)

val documents: Source[List[BsonDocument], Unit] = Source {
  Iterable.tabulate(100) { _ =>
    (1 to 1000).map(i => $document("name" := s"fehmi$i")).toList
  }
}

val result = for {
  insertResult <- collection.insertFromSource(documents).runForeach(_ => ())
  source <- collection.find(BsonDocument.empty)
  count <- source.map(_.size).runFold(0) { (total, size) =>
    total + size
  }
} yield count
```

## Java example

```java
import net.fehmicansaglam.tepkin.api.*;

MongoClient mongoClient = MongoClient.create("mongodb://localhost");
MongoCollection collection = mongoClient.db("tepkin").collection("test");

BsonDocumentBuilder builder = new BsonDocumentBuilder();
builder.addString("name", "fehmi");
BsonDocument document = builder.build();

FiniteDuration timeout = Duration.create(5, TimeUnit.SECONDS);

CompletableFuture<Optional<BsonDocument>> cf = collection
  .insert(document, mongoClient.ec(), timeout)
  .thenCompose(insert -> collection.findOne(mongoClient.ec(), timeout));
Optional<BsonDocument> actual = cf.get(5, TimeUnit.SECONDS);
```

## Give it a try

Current release is *0.1-SNAPSHOT*. So you need to add Sonatype Snapshots repository to your build.sbt:

```scala
resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
```

If you are a Scala developer then add the following dependency:

```scala
libraryDependencies ++= Seq(
  "net.fehmicansaglam" %% "tepkin" % "0.1-SNAPSHOT"
)
```

If you are a Java developer then add the following dependency:

```scala
libraryDependencies ++= Seq(
  "net.fehmicansaglam" %% "tepkin-java" % "0.1-SNAPSHOT"
)
```

_net.fehmicansaglam.tepkin.api_ package is intended to be used from Java.
