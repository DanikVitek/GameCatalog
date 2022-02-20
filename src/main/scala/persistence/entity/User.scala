package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

case class User(id: Long, 
                var firstName: String, 
                var lastName: String, 
                var username: String, 
                var email: Email, 
                private var passwordHash: String)
extends Comparable[User] {
    override def compareTo(o: User): Int = id compareTo o.id
} 
