package de.htwg.se.othello.model.databaseComponent.daoMongoImpl

import de.htwg.se.othello.model.databaseComponent.GameDaoInterface
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observer}
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.ReplaceOptions
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameDao extends GameDaoInterface {

  val mongoClient: MongoClient = MongoClient()

  val database: MongoDatabase = mongoClient.getDatabase("othello")

  val collection: MongoCollection[Document] = database.getCollection("difficultyCollection")

  override def save(difficulty: String): Unit = {

    val difficultyDocument = Document("difficulty" -> difficulty)

    val insertObservable = collection.replaceOne(exists("difficulty", exists = true), difficultyDocument, ReplaceOptions().upsert(true))

    insertObservable.subscribe(new Observer[UpdateResult] {
      override def onNext(result: UpdateResult): Unit = println(s"inserted: $result")
      override def onError(e: Throwable): Unit = println(s"failed: $e")
      override def onComplete(): Unit = println("completed")
    })
  }

  override def load(): String = {
    val document = Await.result(collection.find().toFuture, Duration.Inf).head
    document.getString("difficulty")
  }
}
