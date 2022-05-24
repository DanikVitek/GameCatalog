package edu.mmsa.danikvitek.gamecatalog
package dto

import little.json.*
import little.json.Implicits.{ *, given }

import scala.language.implicitConversions

case class RegistrationDto(firstName: String,
                           lastName: String,
                           username: String,
                           email: String,
                           password: String)

given jsonToRegistrationDto: JsonInput[RegistrationDto] with
    override def apply(json: JsonValue): RegistrationDto =
        RegistrationDto(
            json("firstName"),
            json("lastName"),
            json("username"),
            json("email"),
            json("password")
        )