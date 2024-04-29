package factory

import builder.ElementBuilder
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import model.{Data, Room, RoomState, User}
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement}

object PageFactory {

  def loginFactory(ws: WebSocket[Data, User],
                   inputElement: ReactiveHtmlElement[HTMLInputElement],
                   enterButton:  ReactiveHtmlElement[HTMLButtonElement]): Div = div(
    h2("Register in Agile Poker", cls := "h2-1"),
    div(cls := "form__group field",
      ws.connect,
      label("Name", cls := "form__label"),
      div(inputElement),
      enterButton,
      div(h3("Connected : ", child.text <-- ws.isConnected)),
    ))

  def roomFactory(ws: WebSocket[Data, User], user: Var[User]): Div = {
    val showAll: Var[Boolean] = Var(false)

    div(

      cls := "layoutRoom",
      div(
        cls := "headerRoom",
        div(ElementBuilder.CheckBoxBuilder()
          .withLabel("Display results")
          .withVarBool(showAll)
          .withWs(ws)
          .withUser(user)
          .build())
      ),
      div(
        cls := "middleRoom",
        div(
          cls := "middleLeftRoom"
        ),
        div(
          cls := "middleCenterRoom",
          child <-- ws.received.map(es => {
            TableFactory.tableFactory(user.now(), RoomState(es.room), ws, showAll)
          }
          ),
          CardFactory.allCardsFactory(user.now(), Seq("quart", "demi", "01", "02", "03", "05", "08", "13"), ws)
        ),
        div(
          cls := "middleRightRoom",
          div(children <-- usersBoxed(ws, user))
        )
      ),
      div(
        cls := "footerRoom"
      )
    )
  }

  private def usersBoxed(ws: WebSocket[Data, User], user: Var[User]): EventStream[List[Div]] =
    ws.received.map(data => {
      user.set(data.room.users.filter(u => u.userId == user.now().userId).head)
      data.room.users.sortBy(u => u.userId).map(user =>
        div(userDisplay(user)))
          .toList})


  private def userDisplay(user: User): Div =
    div(cls := "hbox",
      img(src := (if (user.action.result == "None") "others/wait.png" else "others/done.png"),
        width := s"${50 * 0.5}px",
        height := s"${50 * 0.5}px"),
      div(marginLeft := "20px",
        user.userName.split("\\*").last)
    )

}
