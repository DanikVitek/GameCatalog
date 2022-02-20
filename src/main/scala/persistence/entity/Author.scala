package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

import scala.collection.mutable

case class Author(id: Long, 
                  var name: String,
                  var authorType: AuthorType = AuthorType.PERSON,
                  users: mutable.Set[User] = mutable.Set.empty)
extends Comparable[Author] {
    override def compareTo(o: Author): Int = id compareTo o.id
}