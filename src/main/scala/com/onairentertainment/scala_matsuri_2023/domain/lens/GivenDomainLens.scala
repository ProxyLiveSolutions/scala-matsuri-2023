package com.onairentertainment.scala_matsuri_2023.domain.lens

import com.onairentertainment.scala_matsuri_2023.domain.{Address, Balance, City, Country, Crypto, Currency}
import monocle.{Lens, Optional}

object GivenDomainLens:
  given Lens[Address, Country]     = DomainLens.AddressCountryLens
  given Lens[Address, City]        = DomainLens.AddressCityLens
  given Lens[Balance, Currency]    = DomainLens.BalanceCurrencyLens
  given Optional[Currency, Crypto] = DomainLens.CurrencyCryptoOptional
  given Optional[Balance, Crypto]  = DomainLens.BalanceCryptoOptional
