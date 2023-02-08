package com.onairentertainment.scala_matsuri_2023.domain

import org.scalacheck.{Arbitrary, Gen}

object DomainGens {
  val accountTypeGen: Gen[AccountType] = Gen.oneOf(AccountType.values.toSeq)
  val cityGen: Gen[City]               = Gen.alphaNumStr.map(City.apply)
  val countryGen: Gen[Country]         = Gen.oneOf(Country.values.toSeq)
  val addressGen: Gen[Address]         = Gen.zip(countryGen, cityGen).map(Address.apply.tupled)
  val coinsGen: Gen[Coins]             = Gen.oneOf(Coins.values.toSeq)
  val cryptoGen: Gen[Crypto]           = Gen.oneOf(Crypto.values.toSeq)
  val currencyGen: Gen[Currency] =
    val forCoins  = coinsGen.map(Currency.CoinCurr.apply)
    val forCrypto = cryptoGen.map(Currency.CryptoCurr.apply)
    Gen.oneOf(forCoins, forCrypto)

  val balanceGen: Gen[Balance] = Gen.zip(Gen.posNum[Long], currencyGen).map(Balance.apply.tupled)

  given Arbitrary[AccountType] = Arbitrary(accountTypeGen)
  given Arbitrary[City]        = Arbitrary(cityGen)
  given Arbitrary[Country]     = Arbitrary(countryGen)
  given Arbitrary[Address]     = Arbitrary(addressGen)
  given Arbitrary[Coins]       = Arbitrary(coinsGen)
  given Arbitrary[Crypto]      = Arbitrary(cryptoGen)
  given Arbitrary[Currency]    = Arbitrary(currencyGen)
  given Arbitrary[Balance]     = Arbitrary(balanceGen)
}
