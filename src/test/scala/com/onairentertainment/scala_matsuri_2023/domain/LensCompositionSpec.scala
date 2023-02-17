package com.onairentertainment.scala_matsuri_2023.domain

import munit.ScalaCheckSuite
import org.scalacheck.Prop.*
import DomainGens.{*, given}
import cats.syntax.either.*
import com.onairentertainment.scala_matsuri_2023.domain.lens.DomainLens.*
import com.onairentertainment.scala_matsuri_2023.lens.LensDSL.*
import org.scalacheck.cats.instances.GenInstances.*

class LensCompositionSpec extends ScalaCheckSuite:
  override val scalaCheckInitialSeed = "iJo-NQQYFdz8Ty2IwTz4ugiQyxvJrTxNdqRaVltzHrF="

  property("Personal accs are forbidden in Westerlands") {
    val patchedAddressGen = addressGen replace AddressCountryLens by Country.Westerlands

    forAll(patchedAddressGen, balanceGen) { (address, balance) =>
      val result = Account.make(AccountType.Personal, address, balance)

      (result === Left(ValidationError.PersonalAccountsForbidden(Country.Westerlands))) :| s"result = $result"
    }
  }

  property("Crypto is forbidden in Stormlands") {
    val patchedAddressGen = addressGen replace AddressCountryLens by Country.Stormlands
    val patchedBalanceGen = balanceGen replace BalanceCryptoOptional byF cryptoGen

    forAll(accountTypeGen, patchedAddressGen, patchedBalanceGen) { (accountType, address, balance) =>
      val result = Account.make(accountType, address, balance)
      (result === Left(ValidationError.CryptoForbidden(Country.Stormlands))) :| s"result = $result"
    }
  }

  property("Crypto is available only for business in Crownlands") {
    forAll { (address: Address, balance: Balance, anyCrypto: Crypto) =>
      val crownlands    = address.copy(country = Country.Crownlands)
      val cryptoCurr    = Currency.CryptoCurr(anyCrypto)
      val cryptoBalance = balance.copy(currency = cryptoCurr)
      val result        = Account.make(AccountType.Personal, crownlands, cryptoBalance)

      val error = Left(ValidationError.CryptoBusinessOnly(AccountType.Personal, Country.Crownlands))

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
