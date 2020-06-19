package de.htwg.se.othello.model.databaseComponent.daoMongoImpl

import de.htwg.se.othello.model.boardComponent.BoardInterface
import de.htwg.se.othello.model.boardComponent.boardBaseImpl.CreateBoardStrategy
import de.htwg.se.othello.model.databaseComponent.BoardDaoInterface
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.ReplaceOptions
import org.mongodb.scala.result.UpdateResult
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observer}
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class BoardDao extends BoardDaoInterface {

  val mongoClient: MongoClient = MongoClient()

  val database: MongoDatabase = mongoClient.getDatabase("othello")

  val collection: MongoCollection[Document] = database.getCollection("board")

  override def save(board: BoardInterface): Unit = {

    val boardDocument = Document(board.toJson.toString)

    val insertObservable = collection.replaceOne(exists("size", exists = true), boardDocument, ReplaceOptions().upsert(true))

    insertObservable.subscribe(new Observer[UpdateResult] {
      override def onNext(result: UpdateResult): Unit = println(s"inserted: $result")
      override def onError(e: Throwable): Unit = println(s"failed: $e")
      override def onComplete(): Unit = println("completed")
    })
  }

  override def load(): BoardInterface = {
    val document = Await.result(collection.find().toFuture, Duration.Inf).head
    val boardJson = Json.parse(document.toJson)
    (new CreateBoardStrategy).fill(boardJson)
  }
}
