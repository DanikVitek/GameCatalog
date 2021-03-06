package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.connection.ConnectionPool
import persistence.dao.UserDAO
import persistence.entity.{ Email, User }

import com.typesafe.scalalogging.Logger

import java.sql.ResultSet
import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.*

object SQLUserDAO extends UserDAO {
    private final lazy val LOGGER = Logger(SQLUserDAO.getClass)

    private final lazy val TABLE = "users"

    private final lazy val COLUMN_ID = "id"
    private final lazy val COLUMN_FIRST_NAME = "first_name"
    private final lazy val COLUMN_LAST_NAME = "last_name"
    private final lazy val COLUMN_USERNAME = "username"
    private final lazy val COLUMN_EMAIL = "email"
    private final lazy val COLUMN_PASSWORD_HASH = "password_hash"
    private final lazy val COLUMN_SALT = "salt"

    private def extractUserFromRS(rs: ResultSet): User = User.builder
      .withId(rs.getLong(COLUMN_ID))
      .withFirstname(rs.getString(COLUMN_FIRST_NAME))
      .withLastname(rs.getString(COLUMN_LAST_NAME))
      .withUsername(rs.getString(COLUMN_USERNAME))
      .withEmail(Email(rs.getString(COLUMN_EMAIL)))
      .withPasswordHash(rs.getString(COLUMN_PASSWORD_HASH))
      .withSalt(rs.getString(COLUMN_SALT))
      .build

    override def findByUsername(username: String): Option[User] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_USERNAME = ?;")
        ps.setString(1, username)
        val rs = ps.executeQuery

        val result = if rs.next() then Some {
            val user = extractUserFromRS(rs)
            LOGGER info s"Found user by username \"$username\": $user"
            user
        }
        else None

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    override def findByEmail(email: Email): Option[User] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_EMAIL = ?;")
        ps.setString(1, email.value)
        val rs = ps.executeQuery

        val result = if rs.next() then Some {
            val user = extractUserFromRS(rs)
            LOGGER info s"Found user by email \"$email\": $user"
            user
        }
        else None

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    override def findById(id: Long): Option[User] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE where $COLUMN_ID = ?;")
        ps.setLong(1, id)
        val rs = ps.executeQuery

        val result = if rs.next() then Some {
            val user = extractUserFromRS(rs)
            LOGGER info s"Found user by id $id: $user"
            user
        }
        else None

        ps.close()
        ConnectionPool endConnection conn

        result
    }

    override def findAll: List[User] = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"select * from $TABLE;")
        val rs = ps.executeQuery

        @tailrec
        def collectToList(acc: List[User] = Nil): List[User] =
            if rs.next() then collectToList(
                acc :+ {
                    val user = extractUserFromRS(rs)
                    LOGGER info s"One of findAll users: $user"
                    user
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

    override def save(user: User): User = {
        val conn = Await.result(ConnectionPool.startConnection, 1.second)
        val ps = conn.prepareStatement(
            s"insert into $TABLE (" +
              s"$COLUMN_FIRST_NAME, " +
              s"$COLUMN_LAST_NAME, " +
              s"$COLUMN_USERNAME, " +
              s"$COLUMN_EMAIL, " +
              s"$COLUMN_PASSWORD_HASH, " +
              s"$COLUMN_SALT) " +
              s"value (?, ?, ?, ?, ?, ?);"
        )

        ps.setString(1, user.firstName)
        ps.setString(2, user.lastName)
        ps.setString(3, user.username)
        ps.setString(4, user.email.value)
        ps.setString(5, user.passwordHash)
        ps.setString(6, user.salt)

        ps.executeUpdate
        LOGGER info s"Saved user: $user"

        ps.close()
        ConnectionPool endConnection conn

        user.maybeId = Some(findByUsername(user.username).get.id)
        user
    }

    override def update(user: User): User = {
        if user.maybeId.isEmpty then throw new IllegalArgumentException("provided user must have a non null id")

        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(
            s"update ignore $TABLE set " +
              s"$COLUMN_FIRST_NAME = ?, " +
              s"$COLUMN_LAST_NAME = ?, " +
              s"$COLUMN_USERNAME = ?, " +
              s"$COLUMN_EMAIL = ?, " +
              s"$COLUMN_PASSWORD_HASH = ?, " +
              s"$COLUMN_SALT = ? " +
              s"where $COLUMN_ID = ?;"
        )

        ps.setString(1, user.firstName)
        ps.setString(2, user.lastName)
        ps.setString(3, user.username)
        ps.setString(4, user.email.value)
        ps.setString(5, user.passwordHash)
        ps.setString(6, user.salt)
        ps.setLong(7, user.id)

        ps.executeUpdate
        LOGGER info s"Updated user: $user"

        ps.close()
        ConnectionPool endConnection conn

        user
    }

    override def delete(id: Long): Unit = {
        val conn = Await.result(ConnectionPool.startConnection, 0.5.seconds)
        val ps = conn.prepareStatement(s"delete ignore from $TABLE where $COLUMN_ID = ?;")
        ps.setLong(1, id)
        ps.executeUpdate

        ps.close()
        ConnectionPool endConnection conn
        LOGGER info s"Deleted user by id: $id"
    }
}
