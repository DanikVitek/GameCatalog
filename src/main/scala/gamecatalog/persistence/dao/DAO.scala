package edu.mmsa.danikvitek
package gamecatalog.persistence.dao

/**
 * Main Data Access Object trait
 * 
 * @tparam A entity type
 * @tparam I entity ID type
 */
trait DAO[A, I] {
    /**
     * Find an entity by its ID
     * 
     * @param id entity's ID
     * @return Option of entity
     */
    def findById(id: I): Option[A]

    /**
     * @return a list of all existing entities of the given type
     */
    def findAll: List[A]

    /**
     * Saves or updates the given entity in the database
     * 
     * @param entity Entity to save
     * @return saved entity
     */
    def save(entity: A): A

    /**
     * Deletes the entity by the given ID if any
     * 
     * @param id The ID of the entity to delete from database
     */
    def delete(id: I): Unit
}
