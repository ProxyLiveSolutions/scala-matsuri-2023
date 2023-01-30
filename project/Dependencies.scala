import sbt._
import sbt.librarymanagement.ModuleID

object Dependencies {
  private type Deps = Seq[ModuleID]

  private object Cats {
    private val effectVer = "3.3.6"
    private val coreVer   = "2.7.0"
    private val org       = "org.typelevel"

    val core   = org %% "cats-core"   % coreVer
    val effect = org %% "cats-effect" % effectVer
  }

  private object FS2 {
    val core = "co.fs2" %% "fs2-core" % "3.2.7"
  }

  private object Zio {
    val catsInterop = "dev.zio" %% "zio-interop-cats" % "3.3.0"
  }

  val all: Deps = Seq(Cats.core, Cats.effect, FS2.core, Zio.catsInterop)
}
