package com.onairentertainment.scala_matsuri_2023.macros

import scala.quoted.*   // imports Quotes, Expr
import scala.deriving.* // imports Mirror

object MirrorMacros:
  inline def mirrorFields[T]: List[String] = ${ mirrorFieldsImpl[T] }

  def rec[A: Type](using Quotes): List[String] = Type.of[A] match
    case '[field *: fields] => Type.show[field] :: rec[fields]
    case '[EmptyTuple]      => Nil
    case _                  => quotes.reflect.report.errorAndAbort("Expected known tuple but got: " + Type.show[A])

  def mirrorFieldsImpl[T: Type](using Quotes): Expr[List[String]] =
    val ev: Expr[Mirror.Of[T]] = Expr.summon[Mirror.Of[T]].get

    ev match
      case '{ $m: Mirror.ProductOf[T] { type MirroredElemTypes = elementTypes } } => Expr(rec[elementTypes])
      case _ => quotes.reflect.report.errorAndAbort("Expected a case class but got: " + Type.show[T])
