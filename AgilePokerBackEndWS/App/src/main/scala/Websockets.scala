package app

import cask.Logger.Console.globalLogger
import upickle.default._
import main.scala.model.RoomState
import main.scala.model.User
import main.scala.model.Room
import main.scala.model.Data
import main.scala.model.ImplicitCodec._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
//import model.RoomState

object Websockets extends cask.MainRoutes{

  val states: mutable.Map[String, RoomState] = mutable.Map.empty
  states.addOne("1234" -> RoomState(Room(Seq.empty)))

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
        case cask.Ws.Text(json) =>
          val user: User = read[User](json)

          if (!states.apply(roomId).room.users.contains(user)){
            val usersCur: Seq[User] = states.apply(roomId).room.users
            states.addOne(roomId -> RoomState(Room(usersCur.appended(user))))
          }

          val data: Data = Data(user, states.apply(roomId).room)
          println(s"data = $data")
          channel.send(cask.Ws.Text(upickle.default.write(data)))
      }
    }
  }

  @cask.websocket("/room/:roomId/:userName")
  def enterRoom(userName: String, roomId: String): cask.WebsocketResult = {

    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("q!") => channel.send(cask.Ws.Close())
        case cask.Ws.Text(json) =>
          val user: User = read[User](json)
          val data: Data = Data(user, Room(Seq(user)))
          println(s"data = $data")
          channel.send(cask.Ws.Text(write(data)))
      }
    }
  }

  initialize()
}
