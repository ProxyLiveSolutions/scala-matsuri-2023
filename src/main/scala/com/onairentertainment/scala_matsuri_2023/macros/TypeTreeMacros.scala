package com.onairentertainment.scala_matsuri_2023.macros

import scala.quoted.*   // imports Quotes, Expr
import scala.deriving.* // imports Mirror

object TypeTreeMacros:
  inline def typeTree[T]: TypeTree = ${ typeTreeImpl[T] }

  /** qwe */
  private def children[L: Type, T: Type](using Quotes): List[TypeTree.Field] =
    val types  = Type.of[T]
    val labels = Type.of[L]
    (types, labels) match
      case ('[fieldType *: fieldTypes], '[fieldName *: fieldNames]) =>
        val fieldTypeName = Type.show[fieldType]
        val fieldName     = Type.show[fieldName]
        val field         = TypeTree.Field(fieldName = fieldName, typeName = fieldTypeName, children = List.empty)
        field :: children[fieldNames, fieldTypes]
      case ('[EmptyTuple], '[EmptyTuple]) => Nil
      case _ => quotes.reflect.report.errorAndAbort(s"Expected known tuple but got: ${Type.show[L]}; ${Type.show[T]}")

  def typeTreeImpl[T: Type](using Quotes): Expr[TypeTree] =
    val rootName                                = Type.show[T]
    val maybeMirror: Option[Expr[Mirror.Of[T]]] = Expr.summon[Mirror.Of[T]]

    maybeMirror match
      case Some(ev) =>
        ev match
          case '{
                $m: Mirror.ProductOf[T] {
                  type MirroredElemTypes  = elementTypes
                  type MirroredElemLabels = elementLabels
                }
              } =>
            val rootChildren = children[elementLabels, elementTypes]
            Expr(TypeTree.Root(rootName, rootChildren))
          case _ => quotes.reflect.report.errorAndAbort("Expected a case class but got: " + rootName)
      case None => quotes.reflect.report.errorAndAbort(s"Can't get Mirror.Of[$rootName]")
