package app

import cask.Logger.Console.globalLogger
import upickle.default.{ReadWriter => RW, macroRW}

object Websockets extends cask.MainRoutes{

  @cask.get("/hi")
  def hello() = {
    "Hello World!"
  }

  @cask.websocket("/connect/:roomId")
  def enterRoom(roomId: String): cask.WebsocketResult = {

    println(s"in WS for roomId : $roomId")
    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("q!") => channel.send(cask.Ws.Close())
        case cask.Ws.Text(data) =>
          println(data)
          channel.send(cask.Ws.Text(upickle.default.write(User(roomId + " " + data, "????"))))
      }
    }
  }

  @cask.websocket("/room/:roomId/:userName")
  def enterRoom(userName: String, roomId: String): cask.WebsocketResult = {

    println(s"in WS in room : $roomId with user $userName")
    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("q!") => channel.send(cask.Ws.Close())
        case cask.Ws.Text(data) =>
          println(data)
          channel.send(cask.Ws.Text(upickle.default.write(User(roomId + " " + data, "????"))))
      }
    }
  }

  initialize()
}

case class User(userName: String, userId: String)
object User{
  implicit val rw: RW[User] = macroRW
}
