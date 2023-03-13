package com.onairentertainment.scala_matsuri_2023.macros.type_tree_2

import scala.quoted.Expr
import com.onairentertainment.scala_matsuri_2023.macros.ExampleCaseClass

object MacrosMain2:
  def main(args: Array[String]): Unit =
    val fields = TypeTreeMacros.typeTree[ExampleCaseClass]
    val result = TypeTree.prettyPrint(fields)
    println(result)
//    OpticMacros.inspect(scala.sys.error("abort"))
