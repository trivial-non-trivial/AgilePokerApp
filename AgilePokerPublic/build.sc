import mill._, scalalib._

object AgilePokerPublic extends ScalaModule {
  def scalaVersion = "2.13.12"

  def ivyDeps = Agg(
    ivy"io.laminext::websocket_sjs1:0.16.2",
    ivy"io.laminext::websocket-circe_sjs1:0.16.2",
    ivy"io.laminext::fetch-circe_sjs1:0.16.2",
    ivy"com.lihaoyi::upickle:3.1.4"
  )
}
