package edu.mmsa.danikvitek
package gamecatalog.persistence.dao.sql

import gamecatalog.persistence.dao.GameDAO
import gamecatalog.persistence.entity.Game

object SQLGameDAO extends GameDAO {
    override def findByTitle(title: String): Option[Game] = ???

    override def findAllByAuthorName(authorName: String): List[Game] = ???

    override def findById(id: Long): Option[Game] = ???

    override def findAll: List[Game] = ???

    override def save(entity: Game): Game = ???

    override def delete(id: Long): Unit = ???
}
