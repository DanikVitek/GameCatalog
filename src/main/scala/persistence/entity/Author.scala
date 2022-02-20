package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

case class Author(id: Long, 
                  var name: String,
                  var authorType: AuthorType)
extends Comparable[Author] {
    override def compareTo(o: Author): Int = id compareTo o.id
}