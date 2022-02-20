package edu.mmsa.danikvitek.gamecatalog

import persistence.connection.ConnectionPool

import com.typesafe.scalalogging.Logger

import scala.concurrent.{Await, Future}
import concurrent.ExecutionContext.Implicits.global
import concurrent.duration._
import scala.util.Try

object GameCatalogLauncher extends App {
    private final val LOGGER = Logger("GameCatalogLauncher")

    private def onEnable(): Unit = {
        LOGGER info "Launching GameCatalog"
    }

    private def onDisable(): Unit = {
        ConnectionPool.closeConnections()
    }

    private def onUpdate(): Unit = ()


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
