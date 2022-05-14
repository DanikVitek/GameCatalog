package edu.mmsa.danikvitek.gamecatalog
package persistence.dao

import scala.annotation.tailrec

package object sql {
    implicit class ListOps[T](list: List[T]) {
        def *(number: Int): List[T] = {
            @tailrec
            def repeatList(result: List[T] = Nil, repeatsLeft: Int = number): List[T] = {
                if repeatsLeft <= 0 then result
                else repeatList(result ++ list, repeatsLeft - 1)
            }
            repeatList()
        }
    }

    implicit class StrListOps(list: List[String]) extends ListOps(list) {
        def join(connector: String): String = list.foldLeft("")(_ + connector + _)
    }
}
