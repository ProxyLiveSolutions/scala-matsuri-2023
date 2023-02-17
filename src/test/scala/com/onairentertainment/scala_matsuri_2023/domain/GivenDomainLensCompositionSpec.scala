package com.onairentertainment.scala_matsuri_2023.domain

import cats.syntax.either.*
import com.onairentertainment.scala_matsuri_2023.domain.DomainGens.{*, given}
import com.onairentertainment.scala_matsuri_2023.domain.lens.GivenDomainLens.given
import com.onairentertainment.scala_matsuri_2023.lens.GivenLensDSL.*
import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop.*
import org.scalacheck.cats.instances.GenInstances.*

class GivenDomainLensCompositionSpec extends ScalaCheckSuite:
  property("Personal accs are forbidden in Westerlands") {
    val patchedAddressGen = addressGen by Country.Westerlands

    forAll(patchedAddressGen, balanceGen) { (address, balance) =>
      val result = Account.make(AccountType.Personal, address, balance)

      (result === Left(ValidationError.PersonalAccountsForbidden(Country.Westerlands))) :| s"result = $result"
    }
  }

  property("Crypto is forbidden in Stormlands") {
    val patchedAddressGen = addressGen by Country.Stormlands
    val patchedBalanceGen = balanceGen byF cryptoGen

    forAll(accountTypeGen, patchedAddressGen, patchedBalanceGen) { (accountType, address, balance) =>
      val result = Account.make(accountType, address, balance)
      (result === Left(ValidationError.CryptoForbidden(Country.Stormlands))) :| s"result = $result"
    }
  }

  property("Crypto is available only for business in Crownlands") {
    val patchedAddressGen = addressGen by Country.Crownlands
    val patchedBalanceGen = balanceGen byF cryptoGen
    forAll(patchedAddressGen, patchedBalanceGen) { (address, balance) =>
      val result = Account.make(AccountType.Personal, address, balance)
      val error  = Left(ValidationError.CryptoBusinessOnly(AccountType.Personal, address.country))

      (result === error) :| s"result = $result"
    }
  }

  property("FreeCities are free") {
    forAll { (accountType: AccountType, address: Address, balance: Balance) =>
      val freeCities = address.copy(country = Country.FreeCities)
      val result     = Account.make(accountType, freeCities, balance)
      result.isRight :| s"result = $result"
    }
  }
