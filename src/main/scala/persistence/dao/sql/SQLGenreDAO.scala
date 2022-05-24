package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.connection.ConnectionPool
import persistence.dao.{ DAOFactory, GenreDAO }
import persistence.entity.Genre

import com.typesafe.scalalogging.Logger

import java.sql.ResultSet
import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.*

object SQLGenreDAO extends GenreDAO {
    private final lazy val LOGGER = Logger(SQLGenreDAO.getClass)

    private final lazy val TABLE = "genres"
    private final lazy val TABLE_GG_RELATIONS = "game_genre_relations"

    private final lazy val COLUMN_ID = "id"
    private final lazy val COLUMN_TITLE = "title"

    override def findByGameId(gameId: Long): Set[Genre] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(
            s"select * from $TABLE g join $TABLE_GG_RELATIONS r on g.id = r.genre_id where r.game_id = ?"
        )
        ps.setLong(1, gameId)
        val rs = ps.executeQuery

        @tailrec
        def collectToSet(acc: Set[Genre] = Set.empty): Set[Genre] =
            if rs.next then collectToSet(
                acc + {
                    val pair = extractGenreFromRS(rs)
                    LOGGER info s"One of findByGameId($gameId) game-genre pairs: $pair"
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

    override def findById(id: Int): Option[Genre] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_ID = ?;")
        ps.setInt(1, id)
        val rs = ps.executeQuery

        val result = if rs.next then Some {
            val genre = extractGenreFromRS(rs)
            LOGGER info s"Found genre by id $id: $genre"
            genre
        }
        else None

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    override def findAll: List[Genre] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE;")
        val rs = ps.executeQuery

        @tailrec
        def collectToList(acc: List[Genre] = Nil): List[Genre] =
            if rs.next then collectToList(
                acc :+ {
                    val pair = extractGenreFromRS(rs)
                    LOGGER info s"One of findAll game-genre pairs: $pair"
                    pair
                }
            )
            else {
                ps.close()
                ConnectionPool endConnection conn
                acc
            }

        val result = collectToList()

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    private def extractGenreFromRS(rs: ResultSet): Genre = Genre(
        Some(rs.getInt(COLUMN_ID)),
        rs.getString(COLUMN_TITLE)
    )

    override def save(genre: Genre): Genre = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"insert ignore into $TABLE ($COLUMN_TITLE) value (?);")
        ps.setString(1, genre.title)
        ps.executeUpdate
        ps.close()
        ConnectionPool endConnection conn
        genre.maybeId = Some(this.findByTitle(genre.title).get.id)
        genre
    }

    override def findByTitle(title: String): Option[Genre] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_TITLE = ?;")
        ps.setString(1, title)
        val rs = ps.executeQuery

        val result = if rs.next() then Some {
            val genre = extractGenreFromRS(rs)
            LOGGER info s"Found genre by title \"$title\": $genre"
            genre
        }
        else None

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    override def update(genre: Genre): Genre = {
        if genre.maybeId.isEmpty then throw IllegalArgumentException("entity must have a non null id")
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"update ignore $TABLE set $COLUMN_TITLE = ? where $COLUMN_ID = ?;")
        ps.setString(1, genre.title)
        ps.setInt(2, genre.id)
        ps.executeUpdate
        LOGGER info s"updated genre: $genre"
        ps.close()
        ConnectionPool endConnection conn
        genre
    }

    override def delete(id: Int): Unit = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"delete ignore from $TABLE where $COLUMN_ID = ?;")
        ps.setInt(1, id)
        ps.executeUpdate
        LOGGER info s"deleted genre by id $id"
        ps.close()
        ConnectionPool endConnection conn
    }
}
