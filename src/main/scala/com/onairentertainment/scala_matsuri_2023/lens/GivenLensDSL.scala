package com.onairentertainment.scala_matsuri_2023.lens

import com.onairentertainment.scala_matsuri_2023.lens.LensDSL.{ByOptionalStep, BySetterStep}
import monocle.{Optional, Setter}
import cats.syntax.functor.*
import cats.syntax.flatMap.*
import cats.Monad

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
