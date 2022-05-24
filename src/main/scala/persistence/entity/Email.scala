package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

import exception.InvalidEmailException
import persistence.entity.Email.isValidEmail

import java.util.regex.Pattern

case class Email(value: String) {
    if !isValidEmail(value) then throw new InvalidEmailException(s"Email \"$value\" is invalid")
}

object Email {
    private lazy val emailPattern = Pattern.compile(
        "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6}$"
    )

    def isValidEmail(value: String): Boolean = emailPattern.matcher(value).matches()
}