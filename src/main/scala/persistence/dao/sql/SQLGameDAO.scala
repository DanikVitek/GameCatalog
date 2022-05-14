package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.connection.ConnectionPool
import persistence.dao.{AuthorDAO, DAOFactory, GameDAO, GenreDAO}
import persistence.entity.{Game, Genre}

import com.typesafe.scalalogging.Logger

import java.sql.{Connection, PreparedStatement, ResultSet}
import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.*

object SQLGameDAO extends GameDAO {
    private final lazy val LOGGER = Logger(SQLGameDAO.getClass)

    private final lazy val TABLE = "games"
    private final lazy val TABLE_AUTHORS = "authors"
    private final lazy val TABLE_GENRE_RELATIONS = "game_genre_relations"

    private final lazy val COLUMN_ID = "id"
    private final lazy val COLUMN_TITLE = "title"
    private final lazy val COLUMN_AUTHOR = "author"
    private final lazy val COLUMN_PRICE = "price"

    private final lazy val COLUMN_GAME_ID = "game_id"
    private final lazy val COLUMN_GENRE_ID = "genre_id"

    private final lazy val genreDAO: GenreDAO = DAOFactory.getGenreDAO
    private final lazy val authorDAO: AuthorDAO = DAOFactory.getAuthorDAO

    override def findByTitle(title: String): Option[Game] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_TITLE = ?;")
        ps.setString(1, title)
        val rs = ps.executeQuery()

        val result = if rs.next() then Some {
            val id = rs.getLong(COLUMN_ID)
            val game = Game(
                Some(id),
                rs.getString(COLUMN_TITLE),
                genreDAO.findByGameId(id).toSet,
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

    override def findAllByAuthorName(authorName: String): List[Game] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(
            s"select g.* from $TABLE as g join $TABLE_AUTHORS as a on a.id = g.author where a.name = ?;"
        )
        ps.setString(1, authorName)
        val rs = ps.executeQuery()

        collectResults(rs, ps, conn)
    }

    override def findById(id: Long): Option[Game] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_ID = ?;")
        ps.setLong(1, id)
        val rs = ps.executeQuery()

        val result = if rs.next() then Some {
            val game = Game(
                Some(id),
                rs.getString(COLUMN_TITLE),
                genreDAO.findByGameId(id).toSet,
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
        val rs = ps.executeQuery()

        collectResults(rs, ps, conn)
    }

    private def collectResults(rs: ResultSet, ps: PreparedStatement, conn: Connection): List[Game] = {
        @tailrec
        def collectResults(acc: List[Game] = Nil): List[Game] = {
            if rs.next() then collectResults(
                acc :+ {
                    val id = rs.getLong(COLUMN_ID)
                    val game = Game(
                        Some(id),
                        rs.getString(COLUMN_TITLE),
                        genreDAO.findByGameId(id).toSet,
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

        collectResults();
    }

    override def save(game: Game): Game = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(
            s"insert into $TABLE ($COLUMN_TITLE, $COLUMN_AUTHOR, $COLUMN_PRICE) value(?, ?, ?);"
        )
        ps.setString(1, game.title)
        ps.setLong(2, game.author.id)
        ps.setBigDecimal(3, game.price.asInstanceOf[java.math.BigDecimal])

        ps.executeUpdate()

        LOGGER info s"Saved game: $game"

        game.maybeId = Some(findByTitle(game.title).get.id)

        if game.genres.sizeIs > 0 then {
            val psGenres = conn.prepareStatement(
                s"insert into $TABLE_GENRE_RELATIONS ($COLUMN_GAME_ID, $COLUMN_GENRE_ID) " +
                  s"values ${"(?, ?), " * (game.genres.size - 1)}(?, ?);"
            )

            @tailrec
            def saveGenreRelation(i: Int = 0, restOfGenres: Set[Genre] = game.genres): Unit = {
                if restOfGenres.nonEmpty then {
                    psGenres.setLong(i, game.maybeId.get)
                    psGenres.setInt(i + 1, restOfGenres.head.id)
                    saveGenreRelation(i + 2, restOfGenres.tail)
                }
                else psGenres.executeLargeUpdate()
            }

            saveGenreRelation()
        }

        ps.close()
        ConnectionPool endConnection conn

        LOGGER info s"Saved game-genre relations"

        game
    }

    override def update(entity: Game): Game = ???

    override def delete(id: Long): Unit = ???
}
