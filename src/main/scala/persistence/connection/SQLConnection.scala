package edu.mmsa.danikvitek.gamecatalog
package persistence.connection

import org.jetbrains.annotations.NotNull

import java.sql.{ Connection, DriverManager }
import java.util.ResourceBundle
import scala.util.Try

object SQLConnection {
    private final lazy val DRIVER: String = getProperty("driver") getOrElse "org.mariadb.jdbc.Driver"
    private final lazy val HOST: String = getProperty("host") getOrElse "localhost:3306"
    private final lazy val USER: String = getProperty("user") getOrElse "root"
    private final lazy val PASSWORD: String = getProperty("password") getOrElse ""
    private final lazy val DATABASE_NAME: String = getProperty("database_name") getOrElse "game_catalog"
    private final lazy val CONNECTION_URL: String = DRIVER.toLowerCase match {
        case x: String if x.contains("mariadb") => s"jdbc:mariadb://$HOST/$DATABASE_NAME"
    }

    /**
     * A function that returns a connection to the database.
     *
     * @return database connection
     */
    def getConnection: Connection = {
        DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)
    }
}
