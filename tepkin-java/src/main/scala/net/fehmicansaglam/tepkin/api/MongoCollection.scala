package net.fehmicansaglam.tepkin.api

import java.lang.Boolean
import java.util.concurrent.CompletableFuture
import java.util.{List => JavaList, Optional}

import akka.actor.ActorRef
import akka.stream.javadsl.Source
import akka.util.Timeout._
import net.fehmicansaglam.bson.BsonDocument
import net.fehmicansaglam.tepkin
import net.fehmicansaglam.tepkin.api.JavaConverters._
import net.fehmicansaglam.tepkin.api.options.{AggregationOptions, CountOptions}
import net.fehmicansaglam.tepkin.protocol.WriteConcern
import net.fehmicansaglam.tepkin.protocol.command.Index
import net.fehmicansaglam.tepkin.protocol.message.Reply
import net.fehmicansaglam.tepkin.protocol.result._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration


/**
 * Java 8 API for MongoCollection
 * @param proxy Wrapped Scala MongoCollection
 */
class MongoCollection(proxy: tepkin.MongoCollection) {

  /**
   * Calculates aggregate values for the data in this collection.
   *
   * @param pipeline A sequence of data aggregation operations or stages.
   */
  def aggregate(pipeline: JavaList[BsonDocument],
                ec: ExecutionContext,
                timeout: FiniteDuration): CompletableFuture[Source[JavaList[BsonDocument], ActorRef]] = {
    toCompletableFuture {
      proxy.aggregate(pipeline.asScala.toList)(ec, timeout).map(source => Source.adapt(source.map(_.asJava)))(ec)
    }(ec)
  }

  /**
   * Calculates aggregate values for the data in this collection.
   *
   * @param pipeline A sequence of data aggregation operations or stages.
   * @param options Additional options that aggregate() passes to the aggregate command.
   */
  def aggregate(pipeline: JavaList[BsonDocument],
                options: AggregationOptions,
                ec: ExecutionContext,
                timeout: FiniteDuration): CompletableFuture[Source[JavaList[BsonDocument], ActorRef]] = {
    toCompletableFuture {
      proxy.aggregate(pipeline.asScala.toList, options.explain, options.allowDiskUse, options.cursor)(ec, timeout)
        .map(source => Source.adapt(source.map(_.asJava)))(ec)
    }(ec)
  }

  /**
   * Returns the count of documents that would match a find() query. The count() method does not perform
   * the find() operation but instead counts and returns the number of results that match a query.
   */
  def count(ec: ExecutionContext, timeout: FiniteDuration): CompletableFuture[CountResult] = toCompletableFuture {
    proxy.count()(ec, timeout)
  }(ec)

  /**
   * Returns the count of documents that would match a find() query. The count() method does not perform
   * the find() operation but instead counts and returns the number of results that match a query.
   */
  def count(query: BsonDocument,
            ec: ExecutionContext,
            timeout: FiniteDuration): CompletableFuture[CountResult] = toCompletableFuture {
    proxy.count(query = Some(query))(ec, timeout)
  }(ec)

  /**
   * Returns the count of documents that would match a find() query. The count() method does not perform
   * the find() operation but instead counts and returns the number of results that match a query.
   */
  def count(query: BsonDocument,
            options: CountOptions,
            ec: ExecutionContext,
            timeout: FiniteDuration): CompletableFuture[CountResult] = toCompletableFuture {
    proxy.count(query = Some(query), limit = options.limit, skip = options.skip)(ec, timeout)
  }(ec)

  /**
   * Creates indexes on this collection.
   */
  def createIndexes(indexes: JavaList[Index],
                    ec: ExecutionContext,
                    timeout: FiniteDuration): CompletableFuture[CreateIndexesResult] = toCompletableFuture {
    proxy.createIndexes(indexes.asScala: _*)(ec, timeout)
  }(ec)

  def delete(query: BsonDocument,
             ec: ExecutionContext,
             timeout: FiniteDuration): CompletableFuture[DeleteResult] = toCompletableFuture {
    proxy.delete(query)(ec, timeout)
  }(ec)

  /**
   * Finds the distinct values for a specified field across a single collection and returns the results in an array.
   *
   * @param field The field for which to return distinct values.
   */
  def distinct(field: String,
               ec: ExecutionContext,
               timeout: FiniteDuration): CompletableFuture[DistinctResult] = toCompletableFuture {
    proxy.distinct(field)(ec, timeout)
  }(ec)

  /**
   * Finds the distinct values for a specified field across a single collection and returns the results in an array.
   *
   * @param field The field for which to return distinct values.
   * @param query A query that specifies the documents from which to retrieve the distinct values.
   */
  def distinct(field: String,
               query: BsonDocument,
               ec: ExecutionContext,
               timeout: FiniteDuration): CompletableFuture[DistinctResult] = toCompletableFuture {
    proxy.distinct(field, Some(query))(ec, timeout)
  }(ec)

  /** Drops this collection */
  def drop(ec: ExecutionContext, timeout: FiniteDuration): CompletableFuture[Reply] = toCompletableFuture {
    proxy.drop()(ec, timeout)
  }(ec)

  /**
   * Selects documents in this collection.
   */
  def find(query: BsonDocument,
           ec: ExecutionContext,
           timeout: FiniteDuration): CompletableFuture[Source[JavaList[BsonDocument], ActorRef]] = toCompletableFuture {
    proxy.find(query)(ec, timeout).map(source => Source.adapt(source.map(_.asJava)))(ec)
  }(ec)

  /**
   * Selects documents in this collection.
   */
  def find(query: BsonDocument,
           fields: BsonDocument,
           ec: ExecutionContext,
           timeout: FiniteDuration): CompletableFuture[Source[JavaList[BsonDocument], ActorRef]] = toCompletableFuture {
    proxy.find(query, Some(fields))(ec, timeout).map(source => Source.adapt(source.map(_.asJava)))(ec)
  }(ec)

  /**
   * Updates and returns a single document. It returns the old document by default.
   *
   * @param query The selection criteria for the update.
   * @param sort Determines which model the operation updates if the query selects multiple models.
   *             findAndUpdate() updates the first model in the sort order specified by this argument.
   * @param update Performs an update of the selected model.
   * @param returnNew When true, returns the updated model rather than the original.
   * @param fields A subset of fields to return.
   * @param upsert When true, findAndUpdate() creates a new model if no model matches the query.
   */
  def findAndUpdate(query: Optional[BsonDocument],
                    sort: Optional[BsonDocument],
                    update: BsonDocument,
                    returnNew: Boolean,
                    fields: Optional[JavaList[String]],
                    upsert: Boolean,
                    ec: ExecutionContext,
                    timeout: FiniteDuration): CompletableFuture[Optional[BsonDocument]] = toCompletableFuture {
    proxy.findAndUpdate(query, sort, update, returnNew, toOption(fields).map(_.asScala), upsert)(ec, timeout)
      .map(toOptional)(ec)
  }(ec)


  /**
   * Removes and returns a single document.
   *
   * @param query The selection criteria for the remove.
   * @param sort Determines which model the operation removes if the query selects multiple models.
   *             findAndRemove() removes the first model in the sort order specified by this argument.
   * @param fields A subset of fields to return.
   */
  def findAndRemove(query: Optional[BsonDocument],
                    sort: Optional[BsonDocument],
                    fields: Optional[JavaList[String]],
                    ec: ExecutionContext,
                    timeout: FiniteDuration): CompletableFuture[Optional[BsonDocument]] = toCompletableFuture {
    proxy.findAndRemove(query, sort, toOption(fields).map(_.asScala))(ec, timeout).map(toOptional)(ec)
  }(ec)

  /**
   * Retrieves at most one document matching the given selector.
   *
   * @param query Selector document.
   **/
  def findOne(query: BsonDocument,
              ec: ExecutionContext,
              timeout: FiniteDuration): CompletableFuture[Optional[BsonDocument]] = toCompletableFuture {
    proxy.findOne(query)(ec, timeout).map(toOptional)(ec)
  }(ec)

  /**
   * Retrieves at most one document matching the given selector.
   *
   * @param query Selector document.
   * @param skip number of documents to skip. Set to 0 if you don't want to skip any documents.
   **/
  def findOne(query: BsonDocument,
              skip: Integer,
              ec: ExecutionContext,
              timeout: FiniteDuration): CompletableFuture[Optional[BsonDocument]] = toCompletableFuture {
    proxy.findOne(query, skip)(ec, timeout).map(toOptional)(ec)
  }(ec)

  /** Retrieves a random document matching the given selector. */
  def findRandom(query: Optional[BsonDocument],
                 ec: ExecutionContext,
                 timeout: FiniteDuration): CompletableFuture[Optional[BsonDocument]] = toCompletableFuture {
    proxy.findRandom(query)(ec, timeout).map(toOptional)(ec)
  }(ec)

  /**
   * Returns a list of documents that identify and describe the existing indexes on the collection.
   */
  def getIndexes(ec: ExecutionContext,
                 timeout: FiniteDuration): CompletableFuture[JavaList[Index]] = toCompletableFuture {
    proxy.getIndexes()(ec, timeout).map(_.asJava)(ec)
  }(ec)

  def insert(document: BsonDocument,
             ec: ExecutionContext,
             timeout: FiniteDuration): CompletableFuture[InsertResult] = toCompletableFuture {
    proxy.insert(Seq(document))(ec, timeout)
  }(ec)

  def insert(documents: JavaList[BsonDocument],
             ec: ExecutionContext,
             timeout: FiniteDuration): CompletableFuture[InsertResult] = toCompletableFuture {
    proxy.insert(documents.asScala)(ec, timeout)
  }(ec)

  def insertFromSource[M](source: Source[JavaList[BsonDocument], M],
                          ec: ExecutionContext,
                          timeout: FiniteDuration): Source[InsertResult, M] = {
    Source.adapt {
      proxy.insertFromSource[M](source.asScala.map(_.asScala.toList))(ec, timeout)
    }
  }

  def insertFromSource[M](source: Source[JavaList[BsonDocument], M],
                          ordered: Boolean,
                          ec: ExecutionContext,
                          timeout: FiniteDuration): Source[InsertResult, M] = {
    import scala.collection.JavaConverters._
    Source.adapt {
      proxy.insertFromSource[M](source.asScala.map(_.asScala.toList), ordered = Some(ordered))(ec, timeout)
    }
  }

  def insertFromSource[M](source: Source[JavaList[BsonDocument], M],
                          ordered: Boolean,
                          writeConcern: WriteConcern,
                          ec: ExecutionContext,
                          timeout: FiniteDuration): Source[InsertResult, M] = {
    Source.adapt {
      proxy.insertFromSource[M](
        source.asScala.map(_.asScala.toList),
        ordered = Some(ordered),
        writeConcern = Some(writeConcern))(ec, timeout)
    }
  }

  def update(query: BsonDocument,
             update: BsonDocument,
             ec: ExecutionContext,
             timeout: FiniteDuration): CompletableFuture[UpdateResult] = toCompletableFuture {
    proxy.update(query, update)(ec, timeout)
  }(ec)

  def update(query: BsonDocument,
             update: BsonDocument,
             upsert: Boolean,
             ec: ExecutionContext,
             timeout: FiniteDuration): CompletableFuture[UpdateResult] = toCompletableFuture {
    proxy.update(query, update, upsert = Some(upsert))(ec, timeout)
  }(ec)

  def update(query: BsonDocument,
             update: BsonDocument,
             upsert: Boolean,
             multi: Boolean,
             ec: ExecutionContext,
             timeout: FiniteDuration): CompletableFuture[UpdateResult] = toCompletableFuture {
    proxy.update(query, update, upsert = Some(upsert), multi = Some(multi))(ec, timeout)
  }(ec)

  /**
   * Validates this collection. The method scans a collection’s data structures for correctness and returns
   * a single document that describes the relationship between the logical collection
   * and the physical representation of the data.
   */
  def validate(ec: ExecutionContext, timeout: FiniteDuration): CompletableFuture[BsonDocument] = toCompletableFuture {
    proxy.validate(None, None)(ec, timeout)
  }(ec)

  /**
   * Validates this collection. The method scans a collection’s data structures for correctness and returns
   * a single document that describes the relationship between the logical collection
   * and the physical representation of the data.
   *
   * @param full Specify true to enable a full validation and to return full statistics. MongoDB disables full
   *             validation by default because it is a potentially resource-intensive operation.
   */
  def validate(full: Boolean,
               ec: ExecutionContext,
               timeout: FiniteDuration): CompletableFuture[BsonDocument] = toCompletableFuture {
    proxy.validate(Some(full), None)(ec, timeout)
  }(ec)

  /**
   * Validates this collection. The method scans a collection’s data structures for correctness and returns
   * a single document that describes the relationship between the logical collection
   * and the physical representation of the data.
   *
   * @param full Specify true to enable a full validation and to return full statistics. MongoDB disables full
   *             validation by default because it is a potentially resource-intensive operation.
   * @param scandata if false skips the scan of the base collection without skipping the scan of the index.
   */
  def validate(full: Boolean,
               scandata: Boolean,
               ec: ExecutionContext,
               timeout: FiniteDuration): CompletableFuture[BsonDocument] = toCompletableFuture {
    proxy.validate(Some(full), Some(scandata))(ec, timeout)
  }(ec)
}
