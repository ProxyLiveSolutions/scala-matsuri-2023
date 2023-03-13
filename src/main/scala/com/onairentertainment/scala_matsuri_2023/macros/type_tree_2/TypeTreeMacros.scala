package com.onairentertainment.scala_matsuri_2023.macros.type_tree_2

import scala.quoted.*   // imports Quotes, Expr
import scala.deriving.* // imports Mirror

object TypeTreeMacros:
  inline def typeTree[T]: TypeTree.TypeDesc = ${ typeTreeImpl[T] }

  private def children[Labels: Type, Types: Type](using Quotes): List[TypeTree.Field] =
    val types  = Type.of[Types]
    val labels = Type.of[Labels]
    (types, labels) match
      case ('[fieldType *: fieldTypes], '[fieldName *: fieldNames]) =>
        val fieldTypeDesc = typeDesc[fieldType]
        val fieldName     = Type.show[fieldName]
        val field         = TypeTree.Field(fieldName = fieldName, typeDesc = fieldTypeDesc)
        field :: children[fieldNames, fieldTypes]
      case ('[EmptyTuple], '[EmptyTuple]) => Nil
      case _ =>
        quotes.reflect.report.errorAndAbort(s"Expected known tuple but got: ${Type.show[Labels]}; ${Type.show[Types]}")

  private def typeDesc[T: Type](using Quotes): TypeTree.TypeDesc =
    val typeName                                = Type.show[T]
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
            val typeChildren = children[elementLabels, elementTypes]
            TypeTree.TypeDesc(typeName, typeChildren)
          case _ => TypeTree.TypeDesc(typeName, children = List.empty)
      case None => TypeTree.TypeDesc(typeName, children = List.empty)

  private def typeTreeImpl[T: Type](using Quotes): Expr[TypeTree.TypeDesc] = Expr(typeDesc[T])
