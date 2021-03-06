package net.fehmicansaglam.bson

import net.fehmicansaglam.bson.BsonDsl._
import net.fehmicansaglam.bson.Implicits._
import org.scalatest.{Matchers, WordSpec}

class BsonDslSpec extends WordSpec with Matchers {

  "BsonDsl" must {

    "create $unset document" in {
      val expected = "$unset" := {
        ("field1" := "") ~ ("field2" := "")
      }
      val actual = $unset("field1", "field2")
      actual should be(expected)
    }
  }
}
