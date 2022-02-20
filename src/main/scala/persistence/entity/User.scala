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
} 
