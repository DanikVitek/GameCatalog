package edu.mmsa.danikvitek.gamecatalog
package persistence.dao

import persistence.dao.sql.*

object DAOFactory {
    def getGameDAO: GameDAO = SQLGameDAO

    def getUserDAO: UserDAO = SQLUserDAO

    def getGenreDAO: GenreDAO = SQLGenreDAO

    def getAuthorDAO: AuthorDAO = SQLAuthorDAO

    def getGameGenreDAO: GameGenreDAO = SQLGameGenreDAO
}
