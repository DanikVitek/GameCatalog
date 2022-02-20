package edu.mmsa.danikvitek.gamecatalog
package persistence.dao.sql

import persistence.dao.GameDAO
import persistence.entity.Game

object SQLGameDAO extends GameDAO {
    override def findByTitle(title: String): Option[Game] = ???

    override def findAllByAuthorName(authorName: String): List[Game] = ???

    override def findById(id: Long): Option[Game] = ???

    override def findAll: List[Game] = ???

    override def save(entity: Game): Game = ???

    override def delete(id: Long): Unit = ???
}
