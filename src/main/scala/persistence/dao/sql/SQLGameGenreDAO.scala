package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.connection.ConnectionPool
import persistence.dao.GameGenreDAO
import persistence.entity.{ GameGenrePair, Genre }

import com.typesafe.scalalogging.Logger

import java.sql.ResultSet
import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.*

object SQLGameGenreDAO extends GameGenreDAO {
    private final lazy val LOGGER = Logger(SQLGameGenreDAO.getClass)

    private final lazy val TABLE = "game_genre_relations"
    private final lazy val COLUMN_GAME_ID = "game_id"
    private final lazy val COLUMN_GENRE_ID = "genre_id"

    override def findById(id: (Long, Int)): Option[GameGenrePair] = ???

    override def findAll: List[GameGenrePair] = ???

    override def save(entity: GameGenrePair): GameGenrePair = ???

    override def saveAll(pairs: Set[GameGenrePair]): Unit = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(
            s"insert into $TABLE ($COLUMN_GAME_ID, $COLUMN_GENRE_ID) " +
              s"values ${"(?, ?), " * (pairs.size - 1)}(?, ?);"
        )

        @tailrec
        def saveGenreRelation(i: Int = 0, restOfGenres: Set[GameGenrePair] = pairs): Unit = {
            if restOfGenres.nonEmpty then {
                ps.setLong(i, restOfGenres.head.gameId)
                ps.setInt(i + 1, restOfGenres.head.genreId)
                saveGenreRelation(i + 2, restOfGenres.tail)
            }
            else ps.executeLargeUpdate()
        }

        saveGenreRelation()
        LOGGER info s"Saved game-genre relations"

        ps.close()
        ConnectionPool endConnection conn
    }

    override def findByGameId(gameId: Long): Set[GameGenrePair] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE r where r.game_id = ?")
        ps.setLong(1, gameId)
        val rs = ps.executeQuery

        @tailrec
        def collectToSet(acc: Set[GameGenrePair] = Set.empty): Set[GameGenrePair] =
            if rs.next() then collectToSet(
                acc + {
                    val pair = extractPairFromRS(rs)
                    LOGGER info s"One of findByGameId game-genre pairs: $pair"
                    pair
                }
            )
            else {
                ps.close()
                ConnectionPool endConnection conn
                acc
            }

        val result = collectToSet()

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    private def extractPairFromRS(rs: ResultSet): GameGenrePair = GameGenrePair(
        rs.getLong(COLUMN_GAME_ID),
        rs.getInt(COLUMN_GENRE_ID)
    )

    override def update(entity: GameGenrePair): GameGenrePair = ???

    override def delete(id: (Long, Int)): Unit = ???
}
