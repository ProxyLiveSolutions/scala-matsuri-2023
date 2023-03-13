package com.onairentertainment.scala_matsuri_2023.domain.lens

import monocle.*
import monocle.syntax.all.*
import com.onairentertainment.scala_matsuri_2023.domain.*
import monocle.macros.GenLens
import monocle.macros.GenPrism

object DomainLens:
  val AddressCountryLens: Lens[Address, Country]   = GenLens[Address](_.country)
  val AddressCityLens: Lens[Address, City]         = GenLens[Address](_.city)
  val BalanceCurrencyLens: Lens[Balance, Currency] = GenLens[Balance](_.currency)
  val CurrencyCryptoOptional: Optional[Currency, Crypto] = Optional.apply[Currency, Crypto] {
    case c: Currency.CryptoCurr => Some(c.crypto)
    case _                      => None
  } { crypto => _ => Currency.CryptoCurr(crypto) }

  val BalanceCryptoOptional: Optional[Balance, Crypto] =
    BalanceCurrencyLens.andThen(CurrencyCryptoOptional)
