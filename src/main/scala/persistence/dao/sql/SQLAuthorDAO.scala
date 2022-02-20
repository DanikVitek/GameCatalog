package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.dao.AuthorDAO
import persistence.entity.Author

object SQLAuthorDAO extends AuthorDAO {
    override def findByName(name: String): Option[Author] = ???

    override def findById(id: Long): Option[Author] = ???

    override def findAll: List[Author] = ???

    override def save(entity: Author): Author = ???

    override def delete(id: Long): Unit = ???
}
