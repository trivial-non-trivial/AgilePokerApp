import mill._, scalalib._

trait AgilePokerModule extends ScalaModule{
  def scalaVersion = "2.13.12"
}

object AgilePokerBackEndWS extends AgilePokerModule {

  def ivyDeps = Agg(
    ivy"com.lihaoyi::cask:0.9.2",
    ivy"com.lihaoyi::mill-scalalib:0.11.6"
  )
}

object AgilePokerFrontEnd extends AgilePokerModule {

  def ivyDeps = Agg(
    ivy"com.raquo::laminar_sjs1:16.0.0",
    ivy"io.laminext::websocket_sjs1:0.16.2"
  )
}
