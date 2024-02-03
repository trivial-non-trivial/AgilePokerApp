package domAction

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import main.scala.model.{Data, User}
import org.scalajs.dom
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement}
import factory.PageFactory

object DomAction {
  def renderDom(appContainer:  dom.Element,
                ws: WebSocket[Data, User],
                inputElement: ReactiveHtmlElement[HTMLInputElement],
                enterButton:  ReactiveHtmlElement[HTMLButtonElement],
                userName: Var[String]): Unit = {
    render(appContainer, createContent(ws, inputElement, enterButton, userName))
  }

  def createContent(ws: WebSocket[Data, User],
                    inputElement: ReactiveHtmlElement[HTMLInputElement],
                    enterButton: ReactiveHtmlElement[HTMLButtonElement],
                    userName: Var[String]): Div = {

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")
    appContainer.children.foreach(c => appContainer.removeChild(c))

    userName.now() match {
      case "" => PageFactory.loginFactory(ws, inputElement, enterButton)
      case _ => PageFactory.roomFactory(ws, Var(User("Momo", "mkjllkm")))
    }
  }
}
