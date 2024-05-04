package factory

import builder.ElementBuilder
import com.raquo.laminar.api.L._
import handler.ActionHandler
import io.laminext.websocket.upickle.WebSocket
import model.{Data, Room, RoomState, User}

object TableFactory {

  def tableFactory(user: User, room: Room, ws: WebSocket[Data, Data]): Div = {
    div(
      cls := "tableLayout",
      children <-- usersCardBoxed(user, ws, Var(room))
    )
  }

  private def usersCardBoxed(user: User, ws: WebSocket[Data, Data], room: Var[Room]): EventStream[List[Div]] = {
    val ratio = 55.0/100
    val card: Var[String] = Var("")

    println(room.now())

    EventStream.fromValue(room.now().users
      //.filter(_.action.result == "selection_done")
      .filterNot(_.connexionClosed)
      .sortBy(u => u.userId)
      .map(v =>  div(
      cls := "playedCard",
      label(v.userName),
      ElementBuilder.ImageBuilder()
        .withSrc(pickCard(room, user, v))
        .withClass("playedCard")
        .withRatio(ratio)
        .withCard(card)
        .withActionOnClick(ActionHandler.clicActionCard(ws, user, room))
        .build()
    )).toList
    .appended(div(
      cls := "playedCard",
      label(" ... "),
      ElementBuilder.ImageBuilder()
        .withSrc(s"cards/card_v1_empty.png")
        .withClass("playedCard")
        .withRatio(ratio)
        .withCard(card)
        .build())
    ))
  }

  private def pickCard(room: Var[Room], currentUser: User, userInLoop: User): String = {
    if (userInLoop.action.result != "selection_done"){
      s"cards/card_v1_empty.png"
    }
    else if (currentUser.userId == userInLoop.userId || room.now().show) {
      s"cards/${userInLoop.action.input}.png"
    } else s"cards/card_v1_back.png"
  }

}
