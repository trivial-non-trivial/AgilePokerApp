package factory

import builder.ElementBuilder
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
    val ratio = 55.0/100
    val card: Var[String] = Var("")
    EventStream.fromValue(values.map(v =>  div(
      ElementBuilder.ImageBuilder()
        .withSrc(s"cards/card_v1_${v}.png")
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
