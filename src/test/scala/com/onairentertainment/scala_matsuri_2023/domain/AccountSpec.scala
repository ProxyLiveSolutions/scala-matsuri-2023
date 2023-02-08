package com.onairentertainment.scala_matsuri_2023.domain

import munit.ScalaCheckSuite
import org.scalacheck.Prop.*
class AccountSpec extends ScalaCheckSuite:
  property("addition is commutative") {
    forAll { (n1: Int, n2: Int) =>
      n1 + n2 != n2 + n1
    }
  }
