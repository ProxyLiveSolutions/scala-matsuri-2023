package com.onairentertainment.scala_matsuri_2023.domain

import cats.Show
import cats.Eq
import cats.derived.*

enum AccountType derives Eq:
  case Personal
  case Business

final case class City(name: String) derives Eq
enum Country derives Show, Eq:
  case FreeCities
  case Crownlands
  case Stormlands
  case Westerlands

final case class Address(country: Country, city: City) derives Eq

enum Coins derives Eq:
  case Gold
  case Silver
  case Bronze

enum Crypto derives Eq:
  case Bitcoin
  case Monero
  case Doge

enum Currency derives Eq:
  case CoinCurr(fiat: Coins)
  case CryptoCurr(crypto: Crypto)

object Currency:
  def isCrypto(in: Currency): Boolean = in match
    case _: CoinCurr   => false
    case _: CryptoCurr => true

final case class Balance(value: Long, currency: Currency) derives Eq
