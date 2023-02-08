package com.onairentertainment.scala_matsuri_2023.domain

import cats.Eq
import cats.derived.*

enum AccType derives Eq:
  case Personal
  case Business
final case class Account private (accountType: AccType, billAddress: Address, balance: Balance)

object Account:
  import cats.syntax.eq.*
  def make(accountType: AccType, billAddress: Address, balance: Balance): Either[ValidationError, Account] =
    if (
      billAddress.country === Country.Westerlands &&
      accountType === AccType.Personal
    )
      Left(ValidationError.PersonalAccountsForbidden(billAddress.country))
    else if (
      billAddress.country === Country.Crownlands &&
      accountType === AccType.Personal &&
      Currency.isCrypto(balance.currency)
    )
      Left(ValidationError.CryptoBusinessOnly(billAddress.country))
    else if (
      billAddress.country === Country.Stormlands &&
      Currency.isCrypto(balance.currency)
    )
      Left(ValidationError.CryptoForbidden(billAddress.country))
    else
      Right(Account(accountType, billAddress, balance))
