package com.onairentertainment.scala_matsuri_2023.domain

import cats.derived.*
import cats.Eq

final case class Account private (
    accountType: AccountType,
    billAddress: Address,
    balance: Balance
) derives Eq

object Account:
  import cats.syntax.eq.*
  def make(
      accountType: AccountType,
      billAddress: Address,
      balance: Balance
  ): Either[ValidationError, Account] =
    if (
      billAddress.country === Country.Westerlands &&
      accountType === AccountType.Personal
    ) Left(ValidationError.PersonalAccountsForbidden(billAddress.country))
    else if (
      billAddress.country === Country.Crownlands &&
      accountType === AccountType.Personal &&
      Currency.isCrypto(balance.currency)
    ) Left(ValidationError.CryptoBusinessOnly(accountType, billAddress.country))
    else if (
      billAddress.country === Country.Stormlands &&
      Currency.isCrypto(balance.currency)
    ) Left(ValidationError.CryptoForbidden(billAddress.country))
    else
      Right(Account(accountType, billAddress, balance))
