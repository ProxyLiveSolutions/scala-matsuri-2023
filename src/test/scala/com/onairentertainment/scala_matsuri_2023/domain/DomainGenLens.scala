package com.onairentertainment.scala_matsuri_2023.domain

import com.onairentertainment.scala_matsuri_2023.lens.LensLifting.lift
import com.onairentertainment.scala_matsuri_2023.domain.lens.DomainLens
import monocle.*
import org.scalacheck.Gen

object DomainGenLens:
  import org.scalacheck.cats.instances.GenInstances.*

  type LensGen[A, B] = Lens[Gen[A], Gen[B]]

  val AddressCountryLens: LensGen[Address, Country]   = lift(DomainLens.AddressCountryLens)
  val AddressCityLens: LensGen[Address, City]         = lift(DomainLens.AddressCityLens)
  val BalanceCurrencyLens: LensGen[Balance, Currency] = lift(DomainLens.BalanceCurrencyLens)
