package factory

import com.raquo.laminar.api.L._
import main.scala.model.User

object CardFactory {

  def allCardsFactory(user: User, values: Seq[String]): Div = {
    div(
      cls := "allCardsLayout",
      children <-- cardsBoxed(user, values)
    )
  }

  private def cardsBoxed(user: User, values: Seq[String]): EventStream[List[Div]] = {
    val card: Var[String] = Var("")
    EventStream.fromValue(values.map(v =>  div(
      img(
        src := s"cards/card_v1_${v}.png",
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
