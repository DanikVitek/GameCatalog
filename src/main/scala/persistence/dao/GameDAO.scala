package edu.mmsa.danikvitek.gamecatalog
package persistence.dao

import persistence.entity.Game

trait GameDAO extends DAO[Game, Long] {
    /**
     * @param title The title to find the game by
     * @return Option of Game
     */
    def findByTitle(title: String): Option[Game]

    /**
     * Finds all games of a given author
     * 
     * @param authorName The author name to find games by
     * @return the List of Games
     */
    def findAllByAuthorName(authorName: String): List[Game]
}
