package com.onairentertainment.scala_matsuri_2023.domain

import cats.Show
import cats.Eq
import cats.derived.*

final case class City(name: String)
enum Country derives Show, Eq:
  case FreeCities
  case Crownlands
  case Stormlands
  case Westerlands

final case class Address(country: Country, city: City)

enum Coins:
  case Gold
  case Silver
  case Bronze

enum Crypto:
  case Bitcoin
  case Monero
  case Doge

enum Currency:
  case FiatCurr(fiat: Coins)
  case CryptoCurr(crypto: Crypto)

object Currency:
  def isCrypto(in: Currency): Boolean = in match
    case _: FiatCurr   => false
    case _: CryptoCurr => true

final case class Balance(value: Long, currency: Currency)
