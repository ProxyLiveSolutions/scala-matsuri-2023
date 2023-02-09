package com.onairentertainment.scala_matsuri_2023.lens

import monocle.Lens

object LensLifting:
  import cats.Monad
  import cats.syntax.functor.*
  import cats.syntax.flatMap.*

  def lift[F[_]: Monad, A, B](lens: Lens[A, B]): Lens[F[A], F[B]] =
    def get(fa: F[A]): F[B] = fa.map(lens.get)
    def replace(fb: F[B])(fa: F[A]): F[A] = for
      b <- fb
      a <- fa
    yield lens.replace(b)(a)

    Lens.apply[F[A], F[B]](get)(replace)
