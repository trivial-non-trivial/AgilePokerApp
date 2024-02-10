package factory

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
    val card: Var[String] = Var("")
    EventStream.fromValue(state.room.users.map(v =>  div(
      cls := "playedCard",
      label(v.userName),
      img(
        src := s"cards/card_v1_back.png",
        width := s"${200*75/100}px",
        height := s"${300*75/100}px",
        onClick.map(event => {
          println(v + " clicked")
          "out"
        }) --> card
      )
    )).toList)
  }

}
