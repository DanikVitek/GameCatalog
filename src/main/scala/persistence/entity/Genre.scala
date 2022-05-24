package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

case class Genre(var maybeId: Option[Int], title: String)
  extends Comparable[Genre] {
    override def compareTo(o: Genre): Int = id compareTo o.id

    def id: Int = maybeId.get
}
