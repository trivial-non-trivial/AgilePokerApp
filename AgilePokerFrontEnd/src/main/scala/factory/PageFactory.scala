package factory

import builder.ElementBuilder
import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import model.{Data, Room, RoomState, User}
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement}

object PageFactory {

  def loginFactory(ws: WebSocket[Data, Data],
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

  def roomFactory(ws: WebSocket[Data, Data], user: Var[User], room: Var[Room]): Div = {
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
          .withRoom(room)
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
            println(s"## ${es.room}")
            println(s"## ${es.room.show}")
            showAll.set(es.room.show)
            TableFactory.tableFactory(user.now(), es.room, ws)
          }
          ),
          CardFactory.allCardsFactory(user.now(), Seq("quart", "demi", "01", "02", "03", "05", "08", "13"), ws, room)
        ),
        div(
          cls := "middleRightRoom",
          div(children <-- usersBoxed(ws, user, room))
        )
      ),
      div(
        cls := "footerRoom"
      )
    )
  }

  private def usersBoxed(ws: WebSocket[Data, Data], user: Var[User], room: Var[Room]): EventStream[List[Div]] =
    ws.received.map(data => {
      user.set(data.room.users.filter(u => u.userId == user.now().userId).head)
      room.set(data.room)
      data.room.users.sortBy(u => u.userId).map(user =>
        div(userDisplay(user)))
          .toList})


  private def userDisplay(user: User): Div =
    div(cls := "hbox",
      img(src := (
            if (user.connexionClosed) "others/ko.png"
            else if (user.action.result == "None") {
              "others/wait.png"
            } else "others/done.png"),
        width := s"${50 * 0.5}px",
        height := s"${50 * 0.5}px",
        marginTop := "2px",
        marginLeft := "5px"),
      div(marginLeft := "20px",
          marginRight := "10px",
        user.userName.split("\\*").last)
    )

}
