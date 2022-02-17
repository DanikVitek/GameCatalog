package edu.mmsa.danikvitek
package gamecatalog.persistence.dao

import gamecatalog.persistence.entity.Genre

trait GenreDAO extends DAO[Genre, Int] {
    /**
     * Finds the Genre with the given title
     * 
     * @param title The title to find the genre by
     * @return Option of Genre
     */
    def findByTitle(title: String): Option[Genre]
}
