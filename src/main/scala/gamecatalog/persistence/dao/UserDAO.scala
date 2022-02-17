package edu.mmsa.danikvitek
package gamecatalog.persistence.dao

import gamecatalog.persistence.entity.{Email, User}

trait UserDAO extends DAO[User, Long] {
    /**
     * Finds the User by his/her username
     * 
     * @param username The username to find the user by
     * @return Option of User
     */
    def findByUsername(username: String): Option[User]

    /**
     * Finds the User by his/her email
     *
     * @param email The email to find the user by
     * @return Option of User
     */
    def findByEmail(email: Email): Option[User]
}
