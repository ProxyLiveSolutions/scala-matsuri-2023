package com.onairentertainment.scala_matsuri_2023.domain.lens

import monocle.*
import monocle.syntax.all.*
import com.onairentertainment.scala_matsuri_2023.domain.*
import monocle.macros.GenLens

object DomainLens:
  val AddressCountryLens: Lens[Address, Country]   = GenLens[Address](_.country)
  val AddressCityLens: Lens[Address, City]         = GenLens[Address](_.city)
  val BalanceCurrencyLens: Lens[Balance, Currency] = GenLens[Balance](_.currency)

