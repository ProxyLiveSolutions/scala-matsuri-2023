package com.onairentertainment.scala_matsuri_2023.domain

import munit.ScalaCheckSuite
import org.scalacheck.Prop.*
import DomainGens.given
class AccountSpec extends ScalaCheckSuite:
  property("Any account is valid") {
    forAll { (accountType: AccountType, address: Address, balance: Balance) =>
      val result = Account.make(accountType, address, balance)
      result.isRight :| s"result = $result"
    }
  }
