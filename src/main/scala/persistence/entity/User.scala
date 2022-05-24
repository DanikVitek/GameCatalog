package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

import java.security.MessageDigest
import scala.util.Random

case class User(var maybeId: Option[Long] = None,
                var firstName: String,
                var lastName: String,
                var username: String,
                var email: Email,
                var passwordHash: String,
                var salt: String)
  extends Comparable[User] {
    override def compareTo(o: User): Int = id compareTo o.id

    def id: Long = maybeId match {
        case Some(value) => value
        case None => throw new IllegalStateException("Entity is not saved")
    }
}

object User:
    def builder: UserBuilder = new UserBuilder();

private class UserBuilder {
    private var maybeId: Option[Long] = None
    private var firstName: String = _
    private var lastName: String = _
    private var username: String = _
    private var email: Email = _
    private var passwordHash: String = _
    private var salt: String = _

    def withId(id: Long): UserBuilder = this.withMaybeId(Some(id))

    def withMaybeId(maybeId: Option[Long]): UserBuilder = {
        this.maybeId = maybeId
        this
    }

    def withFirstname(firstName: String): UserBuilder = {
        this.firstName = firstName
        this
    }

    def withLastname(lastName: String): UserBuilder = {
        this.lastName = lastName
        this
    }

    def withUsername(username: String): UserBuilder = {
        this.username = username
        this
    }

    def withEmail(email: Email): UserBuilder = {
        this.email = email
        this
    }

    def withPasswordHash(passwordHash: String): UserBuilder = {
        this.passwordHash = passwordHash
        this
    }

    def withSalt(salt: String): UserBuilder = {
        this.salt = salt
        this
    }

    def withPassword(password: String): UserBuilder = {
        this.salt = Random.nextString(8)
        val md = MessageDigest.getInstance("SHA-256")
        md.update((password + this.salt).getBytes())
        this.passwordHash = String(md.digest());
        this
    }

    def build: User = User(
        maybeId, firstName, lastName, username, email, passwordHash, salt
    )
}
