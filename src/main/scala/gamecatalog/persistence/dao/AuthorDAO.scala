package edu.mmsa.danikvitek
package gamecatalog.persistence.dao

import gamecatalog.persistence.entity.Author

trait AuthorDAO extends DAO[Author, Long] {
    /**
     * @param name The name to find the Author by
     * @return Option of Author
     */
    def findByName(name: String): Option[Author]
}
