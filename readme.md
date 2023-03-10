# Composable generators for ScalaCheck

## Table of content:
* Problem definition
* Tech stack
  * ScalaCheck and its generators
  * Monocle
* Composable Gens
  * Manual composition
  * Using Lens
  * Implicit lens
  * Auto derivation
* Links


## Problem definition
First of all, let's start with a problem definition.
Today our problem is next - Bank accounts to Westeros. It's the continent in the fiction world of Game of Thrones. So let's imagine that we want to make a mobile bank for that world.
Unfortunately there are already a lot of regulations there in this regard:
* All personal bank accounts are forbidden in **Westerlands**. Both fiat and crypto.
* **Crownlands** allows using cryptocurrencies only for business and don't for personal use.
* No cryptocurrencies are legal in **Stormlands**. 
* And only **Free Cities** have no regulations for banks.

In Scala code these rules can be expressed like that:

```scala
// Account.scala
def make(accountType: AccountType, billAddress: Address, balance: Balance): Either[ValidationError, Account] =
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
```

And now we see four separate corner cases there - one per each country. In real systems there would be much more of them.
Having such corner cases can make it difficult to use property-based tests to check this logic. The problem here is that now we need four different implementations of `Gen[T]`.  
??? Logical transition ???


## Tech stack
Let's step back a bit and check what tech. stack we are going to use here:
* Scala 3
* ScalaCheck
* Monocle

Scala 3 is used for this project because it's the coolest and the latest version of Scala 😎 Apart from that it provides a bit more concise syntax. But in general all that can be done in Scala 2.13.x as well.

### ScalaCheck and its generators
ScalaCheck is a library for property based testing and one of its main features is the `Gen[T]` abstraction. There are two main features of `Gen[T]` - it randomly generates a value of `T` and can be composed with other `Gen`-s in order to generate more complex values.  
```scala
// DomainGens.scala
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
```
Apart from those combinators above `Gen`s can be combined using _for-comprehensions_(`map(..)` and `flatMap(..)`)

### Monocle

Monocle is a collection of different optics and utilities to work with them. It is needed to simplify work with complex deeply nested case classes - for their modificaitons or getting a piece of information from them.
```scala
// DomainLens.scala
val AddressCountryLens: Lens[Address, Country]   = GenLens[Address](_.country)
val AddressCityLens: Lens[Address, City]         = GenLens[Address](_.city)
val BalanceCurrencyLens: Lens[Balance, Currency] = GenLens[Balance](_.currency)
```

Above is an example of defining monocle's `Lens[.., ..]` using one of the lib's macros.

## Composable Gens

### Manual composition
The simplest way how we can process is to use `.copy(..)` to change generated values in such a way that they would trigger a specific case.

```scala
// ManualCompositionSpec.scala

property("Crypto is forbidden in Stormlands") {
  forAll { (accountType: AccountType, address: Address, balance: Balance, anyCrypto: Crypto) =>
    val stormlands    = address.copy(country = Country.Stormlands)
    val cryptoCurr    = Currency.CryptoCurr(anyCrypto)
    val cryptoBalance = balance.copy(currency = cryptoCurr)
    val result        = Account.make(accountType, stormlands, cryptoBalance)

    (result === Left(ValidationError.CryptoForbidden(Country.Stormlands))) :| s"result = $result"
  }
}
```
This is a good old how to do that. In this case we change particular fields that we are interested in for a particular case. And that can work okey for simple case classes.
Unfortunately for complex domain and deep nested structures that would require way too much of boilerplate code for manually define all `.copy(..)` for each case.

### Using Lens
Monocle should help us to deal with this problem. Instead of using `.copy(..)` directly let's use Monocle and define several optics for our domain.

```scala
// DomainLens.scala
object DomainLens:
  val AddressCountryLens: Lens[Address, Country]   = ???
  val AddressCityLens: Lens[Address, City]         = ???
  val BalanceCurrencyLens: Lens[Balance, Currency] = ???
  val CurrencyCryptoOptional: Optional[Currency, Crypto] = ???
  val BalanceCryptoOptional: Optional[Balance, Crypto] = ???
```
The next step would be to add a small DSL to help with using optics together with `Gen[..]`.
```scala
// LensDSL.scala
object LensDSL:
  // omitted...
  final case class BySetterStep[F[_], A, B](fa: F[A], setter: Setter[A, B]):
    infix def byF(fb: F[B])(using Monad[F]): F[A] = for {
      a <- fa
      b <- fb
    } yield setter.replace(b)(a)
    infix def by(b: B)(using Monad[F]): F[A] = for {
      a <- fa
    } yield setter.replace(b)(a)
  // omitted...
  extension [F[_], A](fa: F[A])
    infix def replace[B](setter: Setter[A, B]): BySetterStep[F, A, B]   = BySetterStep(fa, setter)
    infix def replace[B](opti: Optional[A, B]): ByOptionalStep[F, A, B] = ByOptionalStep(fa, opti)
```

The optics and the DSL makes possible to replace parts of generated values a bit easier.

```scala
property("Crypto is forbidden in Stormlands") {
  val patchedAddressGen = addressGen replace AddressCountryLens by Country.Stormlands
  val patchedBalanceGen = balanceGen replace BalanceCryptoOptional byF cryptoGen

  forAll(accountTypeGen, patchedAddressGen, patchedBalanceGen) { (accountType, address, balance) =>
    val result = Account.make(accountType, address, balance)
    (result === Left(ValidationError.CryptoForbidden(Country.Stormlands))) :| s"result = $result"
  }
}
```

### Implicit lens 
It is possible to reduce amount of code needed to patch a generator. We can define lens as implicits(givens in Scala 3) so the compiler can use a proper one automatically. It is possible as we have all the info about a needed optic at the moment of calling `by` and `byF`

```scala
// GivenLensDSL.scala
/** DSL based on given Lens */
object GivenLensDSL:
  extension [F[_], A](fa: F[A])
    infix def by[B](b: B)(using setter: Setter[A, B], m: Monad[F]): F[A] =
      for a <- fa
    yield setter.replace(b)(a)

    infix def byF[B](fb: F[B])(using opti: Optional[A, B], m: Monad[F]): F[A] = for
      a <- fa
      b <- fb
    yield opti.replace(b)(a)
```
We can use this DSL like this:

```scala
property("Crypto is forbidden in Stormlands") {
  val patchedAddressGen = addressGen by Country.Stormlands
  val patchedBalanceGen = balanceGen byF cryptoGen

  forAll(accountTypeGen, patchedAddressGen, patchedBalanceGen) { (accountType, address, balance) =>
    val result = Account.make(accountType, address, balance)
    (result === Left(ValidationError.CryptoForbidden(Country.Stormlands))) :| s"result = $result"
  }
}
```

### Auto derivation
Further improvement can be archived by introducing lens auto derivation to the project.
For Scala 2.13.x it is possible to do using the tofu library.
For Scala 3 it would require writing macros for that as the library is not yet ported to the latest Scala's version.
That would make unnecessary explicit definition of optics in `DomainLens` and `GivenDomainLens`.

Auto derivation of lens might be difficult though. Or impossible in some cases without changes in the structure of case classes in use. As it would require that all fields in the case classes have unique types among themselves. But this is another topic to explore 😊 

## Links
* [ScalaCheck](https://scalacheck.org/)
* [Monocle](https://www.optics.dev/Monocle/)
* [Scala Tofu](https://docs.tofu.tf/)