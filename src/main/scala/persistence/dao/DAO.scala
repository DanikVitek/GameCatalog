package edu.mmsa.danikvitek.gamecatalog
package persistence.dao

/**
 * Main Data Access Object trait
 * 
 * @tparam A user type
 * @tparam I user ID type
 */
trait DAO[A, I] {
    /**
     * Find an user by its ID
     * 
     * @param id user's ID
     * @return Option of user
     */
    def findById(id: I): Option[A]

    /**
     * @return a list of all existing entities of the given type
     */
    def findAll: List[A]

    /**
     * Saves or updates the given user in the database
     * 
     * @param entity Entity to save
     * @return saved user
     */
    def save(entity: A): A

    /**
     * Deletes the user by the given ID if any
     * 
     * @param id The ID of the user to delete from database
     */
    def delete(id: I): Unit
}
