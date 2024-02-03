import mill._
import scalalib._
import scalajslib._

trait AgilePokerModule extends ScalaModule{
  def scalaVersion = "2.13.12"
}

object AgilePokerBackEndWSModule extends AgilePokerModule {

  // Add (or replace) source folders for the module to use
  override def sources = T.sources {
    super.sources() ++
      Seq(PathRef(build.millSourcePath / "AgilePokerPublic")) ++
      Seq(PathRef(build.millSourcePath  / Seq("AgilePokerBackEndWS", "App")))
  }

  override def mainClass = Some("app.Websockets")

  override def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.9.2",
    ivy"com.lihaoyi::mill-scalalib:0.11.6",
    ivy"com.lihaoyi::upickle:3.1.4"
  )
}

object AgilePokerFrontEndModule extends ScalaJSModule with AgilePokerModule {

  def scalaJSVersion = "1.15.0"

  // Add (or replace) source folders for the module to use
  override def sources = T.sources{
    super.sources() ++ Seq(PathRef(build.millSourcePath / "AgilePokerPublic")) ++
      Seq(PathRef(build.millSourcePath / "AgilePokerFrontEnd"))
  }

  override def mainClass = Some("AgilePokerFrontEnd")

//  def optJs() = T.command {
//      os.proc("sbt", "fastOptJS")
//        .call(cwd = T.workspace / "AgilePokerFrontEnd")
//    }

  override def ivyDeps = Agg(
    ivy"com.raquo::laminar_sjs1:16.0.0",
    ivy"io.laminext::websocket_sjs1:0.16.2",
    ivy"io.laminext::websocket-circe_sjs1:0.16.2",
    ivy"io.laminext::fetch-circe_sjs1:0.16.2",
    ivy"com.lihaoyi::upickle:3.1.4"
  )
}

trait AgilePokerPublicModule extends AgilePokerModule {

  override def ivyDeps = Agg(
    ivy"io.laminext::websocket_sjs1:0.16.2",
    ivy"io.laminext::websocket-circe_sjs1:0.16.2",
    ivy"io.laminext::fetch-circe_sjs1:0.16.2",
    ivy"com.lihaoyi::upickle:3.1.4"
  )
}
