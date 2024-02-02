import mill._
import scalalib._

import scala.collection.Seq

object App extends ScalaModule{
  
  def scalaVersion = "2.13.12"

  // Add (or replace) source folders for the module to use
//  override def sources = T.sources{
//    super.sources() ++ Seq(PathRef(build.millSourcePath / "AgilePokerPublic"))
//  }

  override def sources = T.sources {
    super.sources() ++ Seq(PathRef(os.Path(T.workspace.toIO.getParent) / "AgilePokerPublic"))
  }

  override def ivyDeps = Agg(
      ivy"com.lihaoyi::cask:0.9.2",
      ivy"com.lihaoyi::mill-scalalib:0.11.6",
      ivy"com.lihaoyi::upickle:3.1.4"
    )
}
