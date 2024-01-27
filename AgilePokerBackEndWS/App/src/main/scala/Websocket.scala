package app

import cask.Logger.Console.globalLogger

object Websockets extends cask.MainRoutes{

  @cask.get("/hi")
  def hello() = {
    "Hello World!"
  }

  @cask.websocket("/connect/:userName")
  def showUserProfile(userName: String): cask.WebsocketResult = {

    println("in WS")
    if (userName != "haoyi") cask.Response("", statusCode = 403)
    else cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("!!!!!!!!!!") => channel.send(cask.Ws.Close())
        case cask.Ws.Text(data) =>
          println(data)
          channel.send(cask.Ws.Text(userName + " " + data))
      }
    }
  }

  initialize()
}
