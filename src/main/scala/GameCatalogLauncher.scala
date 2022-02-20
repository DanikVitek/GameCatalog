package edu.mmsa.danikvitek.gamecatalog

import persistence.connection.ConnectionPool
import persistence.dao.UserDAO
import persistence.dao.sql.SQLUserDAO
import persistence.entity.{Email, User}

import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.Try

object GameCatalogLauncher extends App {
    private final val LOGGER = Logger("GameCatalogLauncher")

    private lazy val userDAO: UserDAO = SQLUserDAO

    private def onEnable(): Unit = {
        LOGGER info "Launching GameCatalog"
        userDAO.save(User(
            firstName = "Danik",
            lastName = "Vitek",
            username = "Danik_Vitek",
            email = Email("x3665107@gmail.com"),
            passwordHash = "321654879872135683213546135432135465321"
        ))
    }

    private def onDisable(): Unit = {
        ConnectionPool.closeConnections()
    }

    private def onUpdate(): Unit = ()


    // RUNNING LOGIC
    {
        onEnable()

        val mainRunnable = Future {
            while (true) onUpdate()
        }

        var isStopping = false

        Runtime.getRuntime.addShutdownHook(new Thread(
            () => {
                isStopping = true
                LOGGER info "Stopping GameCatalog"
                onDisable()
                LOGGER info "Stopped GameCatalog"
            },
            "Shutdown Thread")
        )

        while !isStopping do {
            Try(Await.result(mainRunnable, 1.second))
        }
    }
}
