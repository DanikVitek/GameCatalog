package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.connection.ConnectionPool
import persistence.dao.*
import persistence.entity.{ Game, GameGenrePair, Genre }

import com.typesafe.scalalogging.Logger

import java.sql.{ Connection, PreparedStatement, ResultSet }
import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.*

object SQLGameDAO extends GameDAO {
    private final lazy val LOGGER = Logger(SQLGameDAO.getClass)

    private final lazy val TABLE = "games"
    private final lazy val TABLE_AUTHORS = "authors"

    private final lazy val COLUMN_ID = "id"
    private final lazy val COLUMN_TITLE = "title"
    private final lazy val COLUMN_AUTHOR = "author"
    private final lazy val COLUMN_PRICE = "price"

    private final lazy val gameGenreDAO: GameGenreDAO = DAOFactory.getGameGenreDAO
    private final lazy val genreDAO: GenreDAO = DAOFactory.getGenreDAO
    private final lazy val authorDAO: AuthorDAO = DAOFactory.getAuthorDAO

    override def findAllByAuthorName(authorName: String): List[Game] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(
            s"select g.* from $TABLE as g join $TABLE_AUTHORS a on a.id = g.author where a.name = ?;"
        )
        ps.setString(1, authorName)
        val rs = ps.executeQuery

        collectResults(rs, ps, conn)
    }

    override def findById(id: Long): Option[Game] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_ID = ?;")
        ps.setLong(1, id)
        val rs = ps.executeQuery

        val result = if rs.next then Some {
            val game = Game(
                Some(id),
                rs.getString(COLUMN_TITLE),
                genreDAO.findByGameId(id),
                authorDAO.findById(rs.getLong(COLUMN_AUTHOR)).get,
                rs.getBigDecimal(COLUMN_PRICE)
            )
            LOGGER info s"Found game by id $id: $game"
            game
        }
        else None

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    override def findAll: List[Game] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE;")
        val rs = ps.executeQuery

        collectResults(rs, ps, conn)
    }

    private def collectResults(rs: ResultSet, ps: PreparedStatement, conn: Connection): List[Game] = {
        @tailrec
        def collectResults(acc: List[Game] = Nil): List[Game] = {
            if rs.next then collectResults(
                acc :+ {
                    val id = rs.getLong(COLUMN_ID)
                    val game = Game(
                        Some(id),
                        rs.getString(COLUMN_TITLE),
                        genreDAO.findByGameId(id),
                        authorDAO.findById(rs.getLong(COLUMN_AUTHOR)).get,
                        rs.getBigDecimal(COLUMN_PRICE)
                    )
                    LOGGER info s"One of findAll games: $game"
                    game
                }
            )
            else {
                ps.close()
                ConnectionPool endConnection conn
                acc
            }
        }

        collectResults()
    }

    override def save(game: Game): Game = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(
            s"insert into $TABLE ($COLUMN_TITLE, $COLUMN_AUTHOR, $COLUMN_PRICE) value(?, ?, ?);"
        )
        ps.setString(1, game.title)
        ps.setLong(2, game.author.id)
        ps.setBigDecimal(3, game.price.bigDecimal)

        ps.executeUpdate
        LOGGER info s"Saved game: $game"

        ps.close()
        ConnectionPool endConnection conn

        game.maybeId = Some(findByTitle(game.title).get.id)

        if game.genres.sizeIs > 0 then gameGenreDAO.saveAll(
            game.genres
              .map(genre => (game.id, genre.id))
              .map { case (_1, _2) => GameGenrePair(_1, _2) }
        )

        game
    }

    override def findByTitle(title: String): Option[Game] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_TITLE = ?;")
        ps.setString(1, title)
        val rs = ps.executeQuery

        val result = if rs.next then Some {
            val id = rs.getLong(COLUMN_ID)
            val game = Game(
                Some(id),
                rs.getString(COLUMN_TITLE),
                genreDAO.findByGameId(id),
                authorDAO.findById(rs.getLong(COLUMN_AUTHOR)).get,
                rs.getBigDecimal(COLUMN_PRICE)
            )
            LOGGER info s"Found game by title \"$title\": $game"
            game
        }
        else None

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    override def update(game: Game): Game = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(
            s"update ignore $TABLE set " +
              s"$COLUMN_TITLE = ?, " +
              s"$COLUMN_AUTHOR = ?, " +
              s"$COLUMN_PRICE = ? " +
              s"where $COLUMN_ID = ?;"
        )
        ps.setString(1, game.title)
        ps.setLong(2, game.author.id)
        ps.setBigDecimal(3, game.price.bigDecimal)
        ps.setLong(1, game.id)

        ps.executeUpdate
        LOGGER info s"Saved game: $game"

        game
    }

    override def delete(id: Long): Unit = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"delete ignore from $TABLE where $COLUMN_ID = ?;")
        ps.setLong(1, id)

        ps.executeUpdate
        LOGGER info s"Deleted game by id: $id"

        ps.close()
        ConnectionPool endConnection conn
    }
}
