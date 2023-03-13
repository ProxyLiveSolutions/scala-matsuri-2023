package com.onairentertainment.scala_matsuri_2023.macros.type_tree_2

sealed trait TypeTree

object TypeTree:
  final case class TypeDesc(typeName: String, children: List[Field]) extends TypeTree
  final case class Field(fieldName: String, typeDesc: TypeDesc)      extends TypeTree

  import scala.quoted.*
  given ToExpr[TypeDesc] with
    def apply(in: TypeDesc)(using Quotes): Expr[TypeDesc] =
      import quotes.reflect.*
      '{ TypeDesc(${ Expr(in.typeName) }, ${ Expr(in.children) }) }

  given ToExpr[Field] with
    def apply(in: Field)(using Quotes): Expr[Field] =
      import quotes.reflect.*
      '{ Field(${ Expr(in.fieldName) }, ${ Expr(in.typeDesc) }) }

  private def trimName(in: String): String =
    in.split('.').toSeq.toList.last

  private def trimQuotes(in: String): String =
    in.filterNot(_ == '"')

  private def addTabs(in: Int): String = (0 to in).map(_ => " ").mkString("")

  def prettyPrint(in: TypeTree, tab: Int = 0): String = in match
    case TypeDesc(typeName, children) =>
      val trimmed = trimName(typeName)
//      val tabs        = addTabs(tab)
      val forChildren = children.map(prettyPrint(_, tab + 1)).mkString("")
      s"$trimmed:\n" + forChildren
    case Field(fieldName, typeDesc) =>
      val trimmed = trimQuotes(fieldName)
      val forType = prettyPrint(typeDesc, tab + 1)
      val tabs    = addTabs(tab)
      s"$tabs+$trimmed: " + forType
