package edu.mmsa.danikvitek
package gamecatalog.persistence.dao.sql

import gamecatalog.persistence.dao.UserDAO
import gamecatalog.persistence.entity.{Email, User}

object SQLUserDAO extends UserDAO{
    override def findByUsername(username: String): Option[User] = ???

    override def findByEmail(email: Email): Option[User] = ???

    override def findById(id: Long): Option[User] = ???

    override def findAll: List[User] = ???

    override def save(entity: User): User = ???

    override def delete(id: Long): Unit = ???
}
