package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

case class User(var maybeId: Option[Long] = None,
                var firstName: String,
                var lastName: String,
                var username: String,
                var email: Email,
                var passwordHash: String)
extends Comparable[User] {
    override def compareTo(o: User): Int = id compareTo o.id

    def id: Long = maybeId match {
        case Some(value) => value
        case None => throw new IllegalStateException("Entity is not saved")
    }

    class Builder {
        private var maybeId: Option[Long] = None
        private var firstName: String = _
        private var lastName: String = _
        private var username: String = _
        private var email: Email = _
        private var passwordHash: String = _

        def withMaybeId(maybeId: Option[Long]): Builder = {
            this.maybeId = maybeId
            this
        }

        def withFirstname(firstName: String): Builder = {
            this.firstName = firstName
            this
        }
        
        def withLastname(lastName: String): Builder = {
            this.lastName = lastName
            this
        }
        
        def withUsername(username: String): Builder = {
            this.username = username
            this
        }
        
        def withEmail(email: Email): Builder = {
            this.email = email
            this
        }
        
        def withPasswordHash(passwordHash: String): Builder = {
            this.passwordHash = passwordHash
            this
        }
        
        def build: User = User(
            maybeId, firstName, lastName, username, email, passwordHash
        )
    }
}
