package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

import persistence.entity.Email.isValidEmail

import java.util.regex.Pattern

case class Email(value: String) {
    if !isValidEmail(value) then throw new IllegalArgumentException("Invalid email")
}

object Email {
    private lazy val emailPattern = Pattern.compile(
        "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
    )

    def isValidEmail(value: String): Boolean = emailPattern.matcher(value).matches()
}