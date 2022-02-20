package edu.mmsa.danikvitek.gamecatalog
package persistence.dao

import persistence.entity.Author

trait AuthorDAO extends DAO[Author, Long] {
    /**
     * @param name The name to find the Author by
     * @return Option of Author
     */
    def findByName(name: String): Option[Author]
}
