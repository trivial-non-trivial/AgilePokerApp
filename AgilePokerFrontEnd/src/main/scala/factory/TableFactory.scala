package factory

import builder.ElementBuilder
import com.raquo.laminar.api.L._
import main.scala.model.{RoomState, User}

object TableFactory {

  def tableFactory(user: User, state: RoomState): Div = {
    div(
      cls := "tableLayout",
      children <-- usersCardBoxed(user, state)
    )
  }

  private def usersCardBoxed(user: User, state: RoomState): EventStream[List[Div]] = {
    val ratio = 55.0/100
    val card: Var[String] = Var("")
    EventStream.fromValue(state.room.users.map(v =>  div(
      cls := "playedCard",
      label(v.userName),
      ElementBuilder.ImageBuilder()
        .withSrc(s"cards/card_v1_back.png")
        .withClass("playedCard")
        .withRatio(ratio)
        .withCard(card)
        .withActionOnClick(event => {
          println(v + " clicked")
          "out"
        })
        .build()
    )).toList)
  }

}
