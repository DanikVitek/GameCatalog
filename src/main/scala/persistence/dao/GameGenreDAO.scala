package edu.mmsa.danikvitek.gamecatalog
package persistence.dao

import persistence.entity.GameGenrePair

trait GameGenreDAO extends DAO[GameGenrePair, (Long, Int)] :
    def saveAll(pairs: Set[GameGenrePair]): Unit

    def findByGameId(gameId: Long): Set[GameGenrePair]
