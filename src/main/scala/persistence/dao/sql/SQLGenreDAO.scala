package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.dao.GenreDAO
import persistence.entity.Genre

object SQLGenreDAO extends GenreDAO {
    override def findByTitle(title: String): Option[Genre] = ???

    override def findById(id: Int): Option[Genre] = ???

    override def findAll: List[Genre] = ???

    override def save(entity: Genre): Genre = ???

    override def delete(id: Int): Unit = ???
}
