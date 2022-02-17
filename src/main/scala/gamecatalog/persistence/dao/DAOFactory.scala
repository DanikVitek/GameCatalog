package edu.mmsa.danikvitek
package gamecatalog.persistence.dao

import sql.{SQLAuthorDAO, SQLGameDAO, SQLGenreDAO, SQLUserDAO}

object DAOFactory {
    def getGameDAO: GameDAO = SQLGameDAO
    
    def getUserDAO: UserDAO = SQLUserDAO
    
    def getGenreDAO: GenreDAO = SQLGenreDAO
    
    def getAuthorDAO: AuthorDAO = SQLAuthorDAO
}
