package com.onairentertainment.scala_matsuri_2023.domain

import munit.ScalaCheckSuite
import org.scalacheck.Prop.*
import DomainGens.given
import cats.syntax.either.*

class ManualCompositionSpec extends ScalaCheckSuite:
  property("Personal accs are forbidden in Westerlands") {
    forAll { (address: Address, balance: Balance) =>
      val westerlands = address.copy(country = Country.Westerlands)
      val result      = Account.make(AccountType.Personal, westerlands, balance)

      (result === Left(ValidationError.PersonalAccountsForbidden(Country.Westerlands))) :| s"result = $result"
    }
  }

  property("Crypto is forbidden in Stormlands") {
    forAll { (accountType: AccountType, address: Address, balance: Balance, anyCrypto: Crypto) =>
      val stormlands    = address.copy(country = Country.Stormlands)
      val cryptoCurr    = Currency.CryptoCurr(anyCrypto)
      val cryptoBalance = balance.copy(currency = cryptoCurr)
      val result        = Account.make(accountType, stormlands, cryptoBalance)

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
