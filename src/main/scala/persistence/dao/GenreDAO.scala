package edu.mmsa.danikvitek.gamecatalog
package persistence.dao

import persistence.entity.Genre

trait GenreDAO extends DAO[Genre, Int] {
    /**
     * Finds the Genre with the given title
     * 
     * @param title The title to find the genre by
     * @return Option of Genre
     */
    def findByTitle(title: String): Option[Genre]
    
    def findByGameId(gameId: Long): List[Genre]
}
