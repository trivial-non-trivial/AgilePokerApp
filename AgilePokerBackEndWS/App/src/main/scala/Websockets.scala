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
import scala.util.{Failure, Success, Try}

object Websockets extends cask.MainRoutes{

  private val states: mutable.Map[String, RoomState] = mutable.Map.empty
  private val channels: mutable.Map[User, WsChannelActor] = mutable.Map.empty
  private val UIDs: mutable.Map[String, String] = mutable.Map.empty
  private var curUID: Int = 1

  @cask.staticFiles("/agilePoker/:roomId/:filename",
    headers = Seq("Accept" -> "text/css, text/javascript, text/html, image/png, image/avif,image/webp,*/*",
      "Content-Type" -> "text/css, text/javascript, text/html, image/png"))
  def all(roomId: String, filename: String) = {
    Console.println(filename + " ... " + filename.split("\\.").last)

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
    println(s"In getUid for $userName")

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

    Thread.setDefaultUncaughtExceptionHandler((t: Thread, e: Throwable) => println(s"handler 0 = $t $e"))

    println(s"in WS for roomId : $roomId")
    if (!states.contains(roomId)){
      states.addOne(roomId -> RoomState(Room(Seq.empty)))

    }

    cask.WsHandler { channel =>
      cask.WsActor {
        case cask.Ws.Text("q!") => channel.send(cask.Ws.Close())
        case cask.Ws.Text(json) =>
          val user: User = read[User](json)

          if (!states.apply(roomId).room.users.contains(user)){
            val usersCur: Seq[User] = states.apply(roomId).room.users
            states.addOne(roomId -> RoomState(Room(usersCur.appended(user))))

          }

          channels.remove(user)
          channels.addOne(user, channel)

          val data: Data = Data(user, states.apply(roomId).room)
          println(s"data = $data")
          for (user <- states(roomId).room.users) {
            Try{
              channels(user).send(cask.Ws.Text(upickle.default.write(data)))
            }
            match{
              case Failure(exception) =>
                println(s"$exception")
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
