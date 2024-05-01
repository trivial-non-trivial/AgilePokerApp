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
      .filter(_.action.result == "selection_done")
      .sortBy(u => u.userId)
      .map(v =>  div(
      cls := "playedCard",
      label(v.userName),
      ElementBuilder.ImageBuilder()
        .withSrc(if (user.userId == v.userId || room.now().show) s"cards/${v.action.input}.png" else s"cards/card_v1_back.png")
        .withClass("playedCard")
        .withRatio(ratio)
        .withCard(card)
        .withActionOnClick(ActionHandler.clicActionCard(ws, user, room))
        .build()
    )).toList)
  }

}
