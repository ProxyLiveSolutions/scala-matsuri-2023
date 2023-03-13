package com.onairentertainment.scala_matsuri_2023.macros

import scala.quoted.* // imports Quotes, Expr

object OpticMacros:
  def inspectCode[T](x: Expr[T])(using Type[T], Quotes): Expr[T] =
    println(x)
    x

  inline def inspect[T](inline x: T): T = ${ inspectCode('x) }

  inline def power(inline x: Double, inline n: Int) =
    ${ powerCode('x, 'n) }

  def pow(x: Double, n: Int): Double =
    if n == 0 then 1 else x * pow(x, n - 1)

  def powerCode(
      x: Expr[Double],
      n: Expr[Int]
  )(using Quotes): Expr[Double] =
    import quotes.reflect.report
    (x.value, n.value) match
      case (Some(base), Some(exponent)) =>
        val value: Double = pow(base, exponent)
        Expr(value)
      case (Some(_), _) =>
        report.errorAndAbort("Expected a known value for the exponent, but was " + n.show, n)
      case _ =>
        report.errorAndAbort("Expected a known value for the base, but was " + x.show, x)
