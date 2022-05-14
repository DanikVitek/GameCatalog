package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

case class Game(var maybeId: Option[Long],
                var title: String,
                genres: Set[Genre],
                author: Author,
                var price: BigDecimal = BigDecimal(0))
extends Comparable[Game] {
    override def compareTo(o: Game): Int = id compareTo o.id
    
    def id: Long = maybeId.get
}
