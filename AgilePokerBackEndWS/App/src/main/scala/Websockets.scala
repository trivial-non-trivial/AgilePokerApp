package app

import cask.Logger.Console.globalLogger
import cask.WsChannelActor
import cask.util.Ws
import upickle.default._
import upickle.implicits._
import model.RoomState
import model.User
import model.Room
import model.Data
import model.ImplicitCodec.{roomRw, dataRw, userRw}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

object Websockets extends cask.MainRoutes{

  println(s"\nServing at http://localhost:8080/agilePoker/Room-001/index.html \n")

  private val states: mutable.Map[String, RoomState] = mutable.Map.empty
  private val channels: mutable.Map[User, WsChannelActor] = mutable.Map.empty
  private val UIDs: mutable.Map[String, String] = mutable.Map.empty
  private var curUID: Int = 1

  @cask.staticFiles("/agilePoker/:roomId/:filename",
    headers = Seq("Accept" -> "text/css, text/javascript, text/html, image/png, image/avif,image/webp,*/*",
      "Content-Type" -> "text/css, text/javascript, text/html, image/png"))
  def all(roomId: String, filename: String) = {

    filename.split("\\.").last match {
      case "css" => s"AgilePokerBackEndWS/App/src/main/resources/styles/$filename"
      case "png" => s"AgilePokerBackEndWS/App/src/main/resources/cards/$filename"
      case _     => s"AgilePokerBackEndWS/App/src/main/resources/$filename"
    }
  }

  @cask.staticFiles("/favicon.ico",
    headers = Seq("Accept" -> "image/avif"))
  def icone() = {
    s"AgilePokerBackEndWS/App/src/main/resources/favicon.ico"

  }

  @cask.get("/uid/:userName")
  def getUID(userName: String): String = {

    if (UIDs.contains(userName)){
      UIDs(userName)
    }
    else {
      val uid = "UID_" + "%05d".format(curUID)
      UIDs.addOne(userName, uid)
      curUID = curUID + 1
      uid
    }
  }

  @cask.websocket("/connect/:roomId")
  def enterRoom(roomId: String): cask.WebsocketResult = {

    if (!states.contains(roomId)){
      states.addOne(roomId -> RoomState(Room(roomId, Seq.empty, false)))
    }

    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("q!") => channel.send(cask.Ws.Close())
        case cask.Ws.Text(json) =>
          val dataIn: Data = read[Data](json)
          val userIn: User = if(dataIn.user.action.input == "None"
                             && states.apply(roomId).room.users.map(_.userId).contains(dataIn.user.userId)) states.apply(roomId).room.users.find(_.userId == dataIn.user.userId).get
                             else dataIn.user
          val roomIn: Room = if(dataIn.room != null) dataIn.room else states.apply(roomId).room

          if (!states.apply(roomId).room.users.map(_.userId).contains(userIn.userId)){
            val usersCur: Seq[User] = states.apply(roomId).room.users
            states.addOne(roomId -> RoomState(Room(roomId, usersCur.appended(userIn), roomIn.show)))
          }

          channels.remove(userIn)
          channels.addOne(userIn, channel)

          val oldRoom: Room = states.apply(roomId).room
          val room: Room = oldRoom.copy(users = oldRoom.users.filterNot(_.userId == userIn.userId).appended(userIn),
            show = roomIn.show)
          states.addOne(roomId -> RoomState(room))
          val data: Data = Data(userIn, states.apply(roomId).room)
          for (user <- states(roomId).room.users) {
            Try{
              channels(user).run(Ws.Ping("hello".getBytes))
              channels(user).send(cask.Ws.Text(upickle.default.write(data)))
            }
            match{
              case Failure(exception) =>
                println(s"user = $user is closed")
                channels.remove(user)
              case Success(value) =>
                println(s"user = $user is done")
            }
          }
      }
    }
  }



  initialize()
}
