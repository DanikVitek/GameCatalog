package edu.mmsa.danikvitek.gamecatalog
package persistence.connection

import org.jetbrains.annotations.NotNull

import java.util.ResourceBundle
import scala.util.Try

object SQLManager {
    private lazy val resourceBundle = ResourceBundle.getBundle("connection")

    def getProperty(@NotNull name: String): Option[String] = {
        Try(resourceBundle.getString(name)).toOption
    }
}
