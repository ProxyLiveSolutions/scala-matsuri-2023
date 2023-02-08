package com.onairentertainment.scala_matsuri_2023.domain

enum ValidationError:
  /** Country does not allow personal accounts */
  case PersonalAccountsForbidden(country: Country)

  /** All crypto currencies are forbidden */
  case CryptoForbidden(country: Country)

  /** Crypto currencies are allowed only for business accounts */
  case CryptoBusinessOnly(country: Country)
