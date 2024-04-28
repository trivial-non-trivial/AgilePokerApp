import mill._
import mill.scalajslib.api.Report
import os.{Path, home}
import scalalib._
import scalajslib._

trait AgilePokerModule extends ScalaModule{
  def scalaVersion = T {"2.13.12"}
  def scalaJSVersion = T {"1.13.2"}

  val baseDir: Path = Path(millSourcePath.toNIO.getParent)

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
      Seq(PathRef(baseDir / "AgilePokerPublic")) ++
      Seq(PathRef(baseDir  / Seq("AgilePokerBackEndWS", "App")))
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
      Seq(PathRef(baseDir / "AgilePokerPublic")) ++
      Seq(PathRef(baseDir / "AgilePokerFrontEnd"))
  }

  override def fastLinkJS: Target[Report] = {
    val out = super.fastLinkJS

    println("Build:: Done")

    out
  }

  def buildAndCopy = T {

    println("Build:: " + this.fastLinkJS())

    val srcIco = "AgilePokerFrontEnd/src/main/resources/favicon.ico"
    val destIco = "AgilePokerBackEndWS/App/src/main/resources/favicon.ico"
    os.proc("cp", srcIco, destIco).call()
    println("Copy :: " + srcIco)

    val srcHtml = "AgilePokerFrontEnd/src/main/resources/index.html"
    val destHtml = "AgilePokerBackEndWS/App/src/main/resources/index.html"
    os.proc("cp", srcHtml, destHtml).call()
    println("Copy :: " + srcHtml)

    val srcCss = "AgilePokerFrontEnd/src/main/resources/styles"
    val destCss = "AgilePokerBackEndWS/App/src/main/resources/"
    os.proc("cp", "-r", srcCss, destCss).call()
    println("Copy :: " + srcCss)

    val srcCards = "AgilePokerFrontEnd/src/main/resources/cards"
    val destCards = "AgilePokerBackEndWS/App/src/main/resources/"
    os.proc("cp", "-r", srcCards, destCards).call()
    println("Copy :: " + srcCards)

    val srcJs = "out/AgilePokerFrontEndModule/fastLinkJS.dest/main.js"
    val destJs = "AgilePokerBackEndWS/App/src/main/resources/"
    os.proc("cp", srcJs, destJs).call()
    println("Copy :: " + srcJs)

    val srcJsMap = "out/AgilePokerFrontEndModule/fastLinkJS.dest/main.js.map"
    val destJsMap = "AgilePokerBackEndWS/App/src/main/resources/"
    os.proc("cp", srcJsMap, destJsMap).call()
    println("Copy :: " + srcJsMap)

  }

  override def ivyDeps = super.ivyDeps() ++ Seq(
    ivy"com.raquo::laminar::16.0.0"
  )
}

object AgilePokerPublicModule extends AgilePokerModule {

  // Add (or replace) source folders for the module to use
  override def sources = T.sources {
    super.sources() ++
      Seq(PathRef(baseDir / "AgilePokerPublic"))
  }

}
