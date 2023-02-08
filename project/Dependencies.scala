import sbt._
import sbt.librarymanagement.ModuleID

object Dependencies {
  private type Deps = Seq[ModuleID]

  object Cats {
    val version = "2.9.0"
    val groupID = "org.typelevel"

    val core    = groupID         %% "cats-core" % version
    val kittens = "org.typelevel" %% "kittens"   % "3.0.0"
  }

  object Tests {
    val munitCheck = "org.scalameta" %% "munit-scalacheck" % "0.7.29"
  }

  def all: Deps = Seq(Cats.core, Cats.kittens, Tests.munitCheck % Test)
}
