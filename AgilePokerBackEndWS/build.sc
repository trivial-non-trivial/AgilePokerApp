import mill._, scalalib._

object App extends ScalaModule{
  
  def scalaVersion = "2.13.12"

  override def ivyDeps = Agg(
      ivy"com.lihaoyi::cask:0.9.2",
      ivy"com.lihaoyi::mill-scalalib:0.11.6",
      ivy"com.lihaoyi::upickle:3.1.4"
    )
}
