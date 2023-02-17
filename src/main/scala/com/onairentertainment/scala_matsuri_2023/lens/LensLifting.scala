package com.onairentertainment.scala_matsuri_2023.lens

import cats.{Applicative, Functor}
import monocle.*

object LensDSL:
  import cats.Monad
  import cats.syntax.functor.*
  import cats.syntax.flatMap.*
  final case class BySetterStep[F[_], A, B](fa: F[A], setter: Setter[A, B]):
    infix def byF(fb: F[B])(using Monad[F]): F[A] = for {
      a <- fa
      b <- fb
    } yield setter.replace(b)(a)
    infix def by(b: B)(using Monad[F]): F[A] = for {
      a <- fa
    } yield setter.replace(b)(a)

  final case class ByOptionalStep[F[_], A, B](fa: F[A], repl: Optional[A, B]):
    infix def byF(fb: F[B])(using Monad[F]): F[A] = for {
      a <- fa
      b <- fb
    } yield repl.replace(b)(a)

    infix def by(b: B)(using Monad[F]): F[A] = for {
      a <- fa
    } yield repl.replace(b)(a)

  extension [F[_], A](fa: F[A])
    infix def replace[B](setter: Setter[A, B]): BySetterStep[F, A, B]   = BySetterStep(fa, setter)
    infix def replace[B](opti: Optional[A, B]): ByOptionalStep[F, A, B] = ByOptionalStep(fa, opti)
