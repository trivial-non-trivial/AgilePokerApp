import mill._
import scalalib._

import scala.collection.Seq

object AgilePokerFrontEnd extends ScalaModule {
  def scalaVersion = "2.13.12"

  override def sources = T.sources {
    super.sources() ++ Seq(PathRef(os.Path(T.workspace.toIO.getParent) / "AgilePokerPublic"))
  }

//  def run() = T.command {
//    os.proc("sbt", "fastOptJS")
//  }

  def ivyDeps = Agg(
    ivy"com.raquo::laminar_sjs1:16.0.0",
    ivy"io.laminext::websocket_sjs1:0.16.2",
    ivy"io.laminext::websocket-circe_sjs1:0.16.2",
    ivy"io.laminext::fetch-circe_sjs1:0.16.2",
    ivy"com.lihaoyi::upickle:3.1.4"
  )
}
