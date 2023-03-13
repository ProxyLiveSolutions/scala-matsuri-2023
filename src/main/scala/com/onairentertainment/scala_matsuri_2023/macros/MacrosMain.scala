package com.onairentertainment.scala_matsuri_2023.macros

import scala.quoted.Expr

object MacrosMain:
  def main(args: Array[String]): Unit =
    val result = OpticMacros.power(2, 3)
    println(result)
    val fields = TypeTreeMacros.typeTree[ExampleCaseClass]
    println(fields)
//    OpticMacros.inspect(scala.sys.error("abort"))
