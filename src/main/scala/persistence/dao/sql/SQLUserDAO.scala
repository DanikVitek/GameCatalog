package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.dao.UserDAO
import persistence.entity.{Email, User}

object SQLUserDAO extends UserDAO{
    override def findByUsername(username: String): Option[User] = ???

    override def findByEmail(email: Email): Option[User] = ???

    override def findById(id: Long): Option[User] = ???

    override def findAll: List[User] = ???

    override def save(entity: User): User = ???

    override def delete(id: Long): Unit = ???
}