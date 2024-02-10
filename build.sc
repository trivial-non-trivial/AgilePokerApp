import mill._
import mill.scalajslib.api.Report
import os.home
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

  override def fastLinkJS: Target[Report] = {
    val out = super.fastLinkJS

    println("Build:: Done")

    out
  }

  def buildAndCopy = T {

    println("Build:: " + this.fastLinkJS())

    val srcHtml = "AgilePokerFrontEnd/src/main/resources/index.html"
    val destHtml = "AgilePokerBackEndWS/App/src/main/resources/index.html"
    os.proc("cp", srcHtml, destHtml).call()
    println("Copy :: " + srcHtml)

    val srcCss = "AgilePokerFrontEnd/src/main/resources/styles"
    val destCss = "AgilePokerBackEndWS/App/src/main/resources/styles"
    os.proc("cp", "-r", srcCss, destCss).call()
    println("Copy :: " + srcCss)

    val srcJs = "out/AgilePokerFrontEndModule/fastLinkJS.dest/main.js"
    val destJs = "AgilePokerBackEndWS/App/src/main/resources/main.js"
    os.proc("cp", srcJs, destJs).call()
    println("Copy :: " + srcJs)

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
