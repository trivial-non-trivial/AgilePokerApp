package app

import cask.Logger.Console.globalLogger
import cask.WsChannelActor
import upickle.default._
import main.scala.model.RoomState
import main.scala.model.User
import main.scala.model.Room
import main.scala.model.Data
import main.scala.model.ImplicitCodec._

import scala.collection.mutable

object Websockets extends cask.MainRoutes{

  val states: mutable.Map[String, RoomState] = mutable.Map.empty
  val channels: mutable.Map[User, WsChannelActor] = mutable.Map.empty
  states.addOne("1234" -> RoomState(Room(Seq.empty)))

  @cask.staticFiles("/agilePoker/:roomId/index.html",
    headers = Seq("Accept" -> "text/html",
                  "Content-Type" -> "text/html"))
  def index(roomId: String) = {
    println(roomId)
    "AgilePokerBackEndWS/App/src/main/resources/index.html"
  }

  @cask.staticFiles("/agilePoker/:roomId/main.js",
    headers = Seq("Accept" -> "text/javascript"))
  def index2(roomId: String) = {
    println(roomId)
    "AgilePokerBackEndWS/App/src/main/resources/main.js"
  }

  @cask.staticFiles("/agilePoker/:roomId/app.css",
    headers = Seq("Accept" -> "text/css",
                  "Content-Type" -> "text/css"))
  def css(roomId: String) = {
    println(roomId)
    "AgilePokerBackEndWS/App/src/main/resources/app.css"
  }

  @cask.websocket("/connect/:roomId")
  def enterRoom(roomId: String): cask.WebsocketResult = {

    println(s"in WS for roomId : $roomId")

    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("q!") => channel.send(cask.Ws.Close())
        case cask.Ws.Text(json) =>
          val user: User = read[User](json)

          if (!states.apply(roomId).room.users.contains(user)){
            val usersCur: Seq[User] = states.apply(roomId).room.users
            states.addOne(roomId -> RoomState(Room(usersCur.appended(user))))
            channels.addOne(user, channel)
          }

          val data: Data = Data(user, states.apply(roomId).room)
          println(s"data = $data")
          for (user <- states(roomId).room.users) {
            channels(user).send(cask.Ws.Text(upickle.default.write(data)))
          }

      }
    }
  }

  initialize()
}
