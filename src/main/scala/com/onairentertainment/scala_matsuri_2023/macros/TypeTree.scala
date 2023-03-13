package com.onairentertainment.scala_matsuri_2023.macros

import scala.quoted.*

sealed trait TypeTree

object TypeTree:
  final case class Root(typeName: String, children: List[Field])                     extends TypeTree
  final case class Field(fieldName: String, typeName: String, children: List[Field]) extends TypeTree

  given ToExpr[Root] with
    def apply(in: Root)(using Quotes): Expr[Root] =
      import quotes.reflect.*
      '{ Root(${ Expr(in.typeName) }, ${ Expr(in.children) }) }

  given ToExpr[Field] with
    def apply(in: Field)(using Quotes): Expr[Field] =
      import quotes.reflect.*
      '{ Field(${ Expr(in.fieldName) }, ${ Expr(in.typeName) }, ${ Expr(in.children) }) }
