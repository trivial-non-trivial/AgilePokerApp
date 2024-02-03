package factory

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import main.scala.model.{Data, User}
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

  def roomFactory(ws: WebSocket[Data, User], users: Var[User]): Div = div(
    flexDirection := "column",
    div(h3("response from ws")),
    div(children <-- usersBoxed(ws)),
    div(label(child.text <-- ws.received.map(data => {
      Console.println(data)
      //          data.user.userName
      data.toString
    }))),
    div(h3("--------------"))
  )

  private def usersBoxed(ws: WebSocket[Data, User]): EventStream[List[Div]] =
    ws.received.map(data =>
      data.room.users.map(user => div(user.userName.split("\\*").last)).toList)

}
