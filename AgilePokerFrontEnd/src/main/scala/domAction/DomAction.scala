package domAction

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import model.{Action, Data, ImplicitCodec, Room, User}
import org.scalajs.dom
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement}
import factory.PageFactory

object DomAction {
  def renderDom(appContainer:  dom.Element,
                ws: WebSocket[Data, Data],
                inputElement: ReactiveHtmlElement[HTMLInputElement],
                enterButton:  ReactiveHtmlElement[HTMLButtonElement],
                userName: Var[String],
                userId: Var[String],
                room: Var[Room]): Unit = {
    render(appContainer, createContent(ws, inputElement, enterButton, userName, userId, room))
  }

  def createContent(ws: WebSocket[Data, Data],
                    inputElement: ReactiveHtmlElement[HTMLInputElement],
                    enterButton: ReactiveHtmlElement[HTMLButtonElement],
                    userName: Var[String],
                    userId: Var[String],
                    room: Var[Room]): Div = {

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")
    appContainer.children.foreach(c => appContainer.removeChild(c))

    println("userName " + userName.now())

    userName.now() match {
      case "" => PageFactory.loginFactory(ws, inputElement, enterButton)
      case name => PageFactory.roomFactory(ws, Var(User(name, userId.now(), Action[String, String]("in", "out"))), room)
    }
  }
}
