import mill._, scalalib._

object App extends ScalaModule{
  
  def scalaVersion = "2.13.12"

  def ivyDeps = Agg(
      ivy"com.lihaoyi::cask:0.9.2",
      ivy"com.lihaoyi::mill-scalalib:0.11.6"
    )
}
