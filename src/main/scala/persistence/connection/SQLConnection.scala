package edu.mmsa.danikvitek.gamecatalog
package persistence.connection

import java.sql.{Connection, DriverManager}

object SQLConnection {
    /**
     * A function that returns a connection to the database.
     *
     * @return database connection
     */
    def getConnection: Connection = {
        val driver = SQLManager getProperty "driver" getOrElse "org.mariadb.jdbc.Driver"
        val host = SQLManager getProperty "host" getOrElse "localhost:3306"
        val user = SQLManager getProperty "user" getOrElse "root"
        val password = SQLManager getProperty "password" getOrElse ""
        val databaseName = SQLManager getProperty "database_name" getOrElse "game_catalog"
        val url = driver.toLowerCase match {
            case x: String if x.contains("mariadb") => s"jdbc:mariadb://$host/$databaseName"
        }
        DriverManager.getConnection(url, user, password)
    }
}
