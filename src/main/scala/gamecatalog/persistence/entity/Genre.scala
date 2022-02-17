package edu.mmsa.danikvitek
package gamecatalog.persistence.entity

case class Genre(id: Int, 
                 title: String)
extends Comparable[Genre] {
    override def compareTo(o: Genre): Int = id compareTo o.id
}
