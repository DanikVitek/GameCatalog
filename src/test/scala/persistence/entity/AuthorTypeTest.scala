package edu.mmsa.danikvitek.gamecatalog
package persistence.entity

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class AuthorTypeTest extends AnyFlatSpec with should.Matchers {
    behavior of "AuthorType#toString"

    it should "return \"PERSON\" String for PERSON enum" in {
        AuthorType.PERSON.toString shouldEqual "PERSON"
    }

    it should "return \"STUDIO\" String for STUDIO enum" in {
        AuthorType.STUDIO.toString shouldEqual "STUDIO"
    }
}
