package factory

import builder.ElementBuilder
import com.raquo.laminar.api.L._
import handler.ActionHandler
import io.laminext.websocket.upickle.WebSocket
import model.{Data, RoomState, User}

object TableFactory {

  def tableFactory(user: User, state: RoomState, ws: WebSocket[Data, User]): Div = {
    div(
      cls := "tableLayout",
      children <-- usersCardBoxed(user, state, ws)
    )
  }

  private def usersCardBoxed(user: User, state: RoomState, ws: WebSocket[Data, User]): EventStream[List[Div]] = {
    val ratio = 55.0/100
    val card: Var[String] = Var("")
    EventStream.fromValue(state.room.users.filter(_.action.result == "selection_done").map(v =>  div(
      cls := "playedCard",
      label(v.userName),
      ElementBuilder.ImageBuilder()
        .withSrc(s"cards/card_v1_back.png")
        .withClass("playedCard")
        .withRatio(ratio)
        .withCard(card)
        .withActionOnClick(ActionHandler.clicActionCard(ws, user, roomId = "roommm"))
        .build()
    )).toList)
  }

}
