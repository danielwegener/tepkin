package net.fehmicansaglam.tepkin.protocol.command

import net.fehmicansaglam.tepkin.bson.BsonDocument
import net.fehmicansaglam.tepkin.bson.BsonDsl._
import net.fehmicansaglam.tepkin.bson.Implicits._

case class Update(databaseName: String,
                  collectionName: String,
                  updates: Seq[UpdateElement],
                  ordered: Option[Boolean] = None,
                  writeConcern: Option[BsonDocument] = None) extends Command {
  override val command: BsonDocument = {
    ("update" := collectionName) ~
      ("updates" := $array(updates.map(_.asBsonDocument): _*)) ~
      ("ordered" := ordered) ~
      ("writeConcern" := writeConcern)
  }
}

case class UpdateElement(q: BsonDocument,
                         u: BsonDocument,
                         upsert: Option[Boolean] = None,
                         multi: Option[Boolean] = None) {
  val asBsonDocument: BsonDocument = {
    ("q" := q) ~
      ("u" := u) ~
      ("upsert" := upsert) ~
      ("multi" := multi)
  }
}
