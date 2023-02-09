import sbt._
import sbt.librarymanagement.ModuleID

object Dependencies {
  private type Deps = Seq[ModuleID]

  object Cats {
    val groupID = "org.typelevel"

    val core    = groupID %% "cats-core" % "2.9.0"
    val kittens = groupID %% "kittens"   % "3.0.0"
  }

  object Tests {
    val munitCheck     = "org.scalameta"     %% "munit-scalacheck" % "0.7.29"
    val scalaCheckCats = "io.chrisdavenport" %% "cats-scalacheck"  % "0.3.2"
  }

  object Monocle {
    private val version = "3.1.0"
    private val groupID = "dev.optics"

    val core   = groupID %% "monocle-core"  % version
    val macros = groupID %% "monocle-macro" % version
  }

  def all: Deps = Seq(
    Cats.core,
    Cats.kittens,
    Monocle.core,
    Monocle.macros,
    Tests.munitCheck     % Test,
    Tests.scalaCheckCats % Test
  )
}
