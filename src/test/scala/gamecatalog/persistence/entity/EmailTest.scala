package edu.mmsa.danikvitek
package gamecatalog.persistence.entity

import org.scalatest.Inspectors.forAll
import org.scalatest.exceptions.TestFailedException
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.util.{Failure, Success, Try}

class EmailTest extends AnyFlatSpec with should.Matchers {
    private lazy val validEmails = List(
        "user@domain.com",
        "user@domain.co.in",
        "user.name@domain.com",
        "user.name@domain.com",
        "username@yahoo.corporate.in"
    )

    private lazy val invalidEmails = List(
        ".username@yahoo.com",
        "username@yahoo.com.",
        "username@yahoo..com",
        "username@yahoo.c",
        "username@yahoo.corporate"
    )

    "List of valid emails" should "match email pattern" in {
        validEmails foreach (Email.isValidEmail(_) should be(true))
    }

    it should "not throw IllegalArgumentException for all elements" in {
        validEmails foreach (value => Try(Email(value)).isSuccess should be(true))
    }

    "List of invalid emails" should "not match email pattern" in {
        invalidEmails foreach (Email.isValidEmail(_) should be(false))
    }

    it should "throw IllegalArgumentException for all elements" in {
        invalidEmails foreach { value =>
            Try(Email(value)) match {
                case Failure(exception) => exception shouldBe an [IllegalArgumentException]
                case Success(value) =>
                    throw new TestFailedException("was Success but Failure expected", 50)
            }
        }
    }
}
