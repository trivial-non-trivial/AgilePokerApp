package factory

import builder.ElementBuilder
import com.raquo.laminar.api.L._
import handler.ActionHandler
import io.laminext.websocket.upickle.WebSocket
import model.{Data, RoomState, User}

object TableFactory {

  def tableFactory(user: User, state: RoomState, ws: WebSocket[Data, User], showAll: Var[Boolean]): Div = {
    div(
      cls := "tableLayout",
      children <-- usersCardBoxed(user, state, ws, showAll)
    )
  }

  private def usersCardBoxed(user: User, state: RoomState, ws: WebSocket[Data, User], showAll: Var[Boolean]): EventStream[List[Div]] = {
    val ratio = 55.0/100
    val card: Var[String] = Var("")
    EventStream.fromValue(state.room.users
      .filter(_.action.result == "selection_done")
      .sortBy(u => u.userId)
      .map(v =>  div(
      cls := "playedCard",
      label(v.userName),
      ElementBuilder.ImageBuilder()
        .withSrc(if (user.userId == v.userId || showAll.now()) s"cards/${v.action.input}.png" else s"cards/card_v1_back.png")
        .withClass("playedCard")
        .withRatio(ratio)
        .withCard(card)
        .withActionOnClick(ActionHandler.clicActionCard(ws, user, roomId = "roommm"))
        .build()
    )).toList)
  }

}
