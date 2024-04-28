package domAction

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import model.{Data, User, ImplicitCodec, Action}
import org.scalajs.dom
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement}
import factory.PageFactory

object DomAction {
  def renderDom(appContainer:  dom.Element,
                ws: WebSocket[Data, User],
                inputElement: ReactiveHtmlElement[HTMLInputElement],
                enterButton:  ReactiveHtmlElement[HTMLButtonElement],
                userName: Var[String],
                userId: Var[String]): Unit = {
    render(appContainer, createContent(ws, inputElement, enterButton, userName, userId))
  }

  def createContent(ws: WebSocket[Data, User],
                    inputElement: ReactiveHtmlElement[HTMLInputElement],
                    enterButton: ReactiveHtmlElement[HTMLButtonElement],
                    userName: Var[String],
                    userId: Var[String]): Div = {

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")
    appContainer.children.foreach(c => appContainer.removeChild(c))

    println("userName " + userName.now())

    userName.now() match {
      case "" => PageFactory.loginFactory(ws, inputElement, enterButton)
      case name => PageFactory.roomFactory(ws, Var(User(name, userId.now(), Action[String, String]("in", "out"))))
    }
  }
}
