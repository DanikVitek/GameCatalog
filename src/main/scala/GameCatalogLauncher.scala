package edu.mmsa.danikvitek.gamecatalog

import persistence.connection.ConnectionPool
import persistence.dao.{ DAOFactory, UserDAO }
import persistence.entity.{ Email, User, UserBuilder }

import com.typesafe.scalalogging.Logger

import java.lang.Thread.onSpinWait
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.concurrent.{ Await, Future }
import scala.language.postfixOps
import scala.util.Try

object GameCatalogLauncher extends App {
    private final val LOGGER = Logger(GameCatalogLauncher.getClass)

    private lazy val userDAO: UserDAO = DAOFactory.getUserDAO

    private def onEnable(): Unit = {
        LOGGER info "Launching GameCatalog"
        userDAO.save(
            User.builder
              .withFirstname("Danik")
              .withLastname("Vitek")
              .withUsername("Danik_Vitek")
              .withEmail(Email("x3665107@gmail.com"))
              .withPassword("12345678")
              .build
        )
    }

    private def onDisable(): Unit = {
        ConnectionPool.closeConnections()
    }

    private def onUpdate(): Unit = ()


    // RUNNING LOGIC
    {
        onEnable()

        val mainRunnable = Future {
            while (true) {
                onUpdate()
                onSpinWait()
            }
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
