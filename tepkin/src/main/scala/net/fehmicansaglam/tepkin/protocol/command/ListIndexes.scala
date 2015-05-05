package net.fehmicansaglam.tepkin.protocol.command

import net.fehmicansaglam.bson.BsonDocument
import net.fehmicansaglam.bson.BsonDsl._

case class ListIndexes(databaseName: String,
                       collectionName: String) extends Command {
  override def command: BsonDocument = "listIndexes" := collectionName
}
