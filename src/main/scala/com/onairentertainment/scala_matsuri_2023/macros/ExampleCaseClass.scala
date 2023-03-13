package com.onairentertainment.scala_matsuri_2023.macros

final case class ExampleCaseClass(case1: ComplexCase1, case2: ComplexCase2)

final case class ComplexCase1(int: NestedInt, long: NestedLong)

final case class ComplexCase2(int: NestedInt, long: NestedLong)

final case class NestedInt(value: Int)

final case class NestedLong(value: Long)
