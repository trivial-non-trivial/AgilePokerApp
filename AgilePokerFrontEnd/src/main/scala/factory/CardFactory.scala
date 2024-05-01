package factory

import builder.ElementBuilder
import com.raquo.laminar.api.L._
import handler.ActionHandler
import io.laminext.websocket.upickle.WebSocket
import model.{Data, Room, RoomState, User}

object CardFactory {

  def allCardsFactory(user: User, values: Seq[String], ws: WebSocket[Data, Data]): Div = {
    val room: Var[Room] = Var(Room("", Seq.empty, false))
    ws.received.map(es => es.room) --> room
    div(
      cls := "allCardsLayout",
      children <-- cardsBoxed(user, values, ws, room)
    )
  }

  private def cardsBoxed(user: User, values: Seq[String], ws: WebSocket[Data, Data], room: Var[Room]): EventStream[List[Div]] = {
    val ratio = 55.0/100
    val card: Var[String] = Var("")
    EventStream.fromValue(values.map(v =>  div(
      ElementBuilder.ImageBuilder()
        .withSrc(s"cards/card_v1_${v}.png")
        .withClass("playedCard")
        .withRatio(ratio)
        .withCard(card)
        .withActionOnClick2(ActionHandler.clicActionCard(ws, user, room))
        .build()
    )).toList)
  }

}
