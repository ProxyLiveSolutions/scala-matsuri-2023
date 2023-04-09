package com.onairentertainment.scala_matsuri_2023.state

import scala.reflect.ClassTag

sealed trait Event
object Event:
  final case class FundGameStarted()   extends Event
  final case class FundGameConfirmed() extends Event
  final case class CancelStarted()     extends Event

sealed trait Command
object Command:
  case object FundGame        extends Command
  case object ConfirmFundGame extends Command
  case object FailFundGame    extends Command
  case object Settle          extends Command
  case object ConfirmSettle   extends Command
  case object FailSettle      extends Command

sealed trait TxState
object TxState:
  case object DefaultState                         extends TxState
  case object ActivePending                        extends TxState
  final case class ActiveConfirmed(ticket: String) extends TxState
  case object ActiveFailed                         extends TxState
  case object ActiveCanceled                       extends TxState
  case object SettledPending                       extends TxState
  case object SettledConfirmed                     extends TxState
  case object SettledFailed                        extends TxState
  case object CanceledPending                      extends TxState
  case object CanceledConfirmed                    extends TxState
  case object CanceledFailed                       extends TxState

sealed trait Tr[-In, +Start, +End, +Out]

object Tr:
  import Command.*
  import TxState.*
  import Event.*
  case object FundGameWhileNone extends Tr[FundGame.type, DefaultState.type, ActivePending.type, FundGameStarted]
  case object ConfirmFundGameWhileActivePending
      extends Tr[ConfirmFundGame.type, ActivePending.type, ActiveConfirmed, FundGameConfirmed]
  case object ConfirmFundGameWhileActiveCanceled
      extends Tr[ConfirmFundGame.type, ActiveCanceled.type, CanceledPending.type, CancelStarted]

final case class Graph[In, State, Out](allTrs: Map[(String, String), Tr[In, State, State, Out]]):
  def add[In1 >: In: ClassTag, Start <: State: ClassTag, End <: State, Out1 <: State](
      tr: Tr[In1, Start, End, Out]
  ): Graph[In, State, Out] =
    val inName    = summon[ClassTag[In1]].runtimeClass.getName
    val stateName = summon[ClassTag[Start]].runtimeClass.getName
    Graph(allTrs.updated((inName, stateName), tr))
