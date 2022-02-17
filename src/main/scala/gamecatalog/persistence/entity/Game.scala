package edu.mmsa.danikvitek
package gamecatalog.persistence.entity

import scala.collection.mutable

case class Game(id: Long,
                var title: String,
                genres: mutable.Set[Genre],
                author: Author,
                var price: BigDecimal = BigDecimal(0))
extends Comparable[Game] {
    override def compareTo(o: Game): Int = id compareTo o.id
}
