import mill._
import scalalib._
import scalajslib._

trait AgilePokerModule extends ScalaModule{
  def scalaVersion = T {"2.13.12"}
  def scalaJSVersion = T {"1.13.2"}

  override def ivyDeps = Agg(
    ivy"io.laminext::websocket_sjs1:0.16.0",
    ivy"io.laminext::websocket-upickle_sjs1:0.16.0",
    ivy"io.laminext::fetch-upickle_sjs1:0.16.0",
    ivy"com.lihaoyi::upickle:3.1.1"
  )
}

object AgilePokerBackEndWSModule extends AgilePokerModule {

  // Add (or replace) source folders for the module to use
  override def sources = T.sources {
    super.sources() ++
      Seq(PathRef(build.millSourcePath / "AgilePokerPublic")) ++
      Seq(PathRef(build.millSourcePath  / Seq("AgilePokerBackEndWS", "App")))
  }

  override def mainClass = Some("app.Websockets")

  override def ivyDeps = super.ivyDeps() ++ Seq(
    ivy"com.lihaoyi::cask:0.9.2",
    ivy"com.lihaoyi::mill-scalalib:0.11.6"
  )
}

object AgilePokerFrontEndModule extends ScalaJSModule with AgilePokerModule {

  // Add (or replace) source folders for the module to use
  override def sources = T.sources{
    super.sources() ++
      Seq(PathRef(build.millSourcePath / "AgilePokerPublic")) ++
      Seq(PathRef(build.millSourcePath / "AgilePokerFrontEnd"))
  }

  override def ivyDeps = super.ivyDeps() ++ Seq(
    ivy"com.raquo::laminar::16.0.0"
  )
}

object AgilePokerPublicModule extends AgilePokerModule {

  // Add (or replace) source folders for the module to use
  override def sources = T.sources {
    super.sources() ++
      Seq(PathRef(build.millSourcePath / "AgilePokerPublic"))
  }

}
