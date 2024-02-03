import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import org.scalajs.dom
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement}
import main.scala.model.{Data, User}

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
      case "" => div(
        h2("Register in Agile Poker", cls := "h2-1"),
        div(cls := "form__group field",
          ws.connect,
          label("Name", cls := "form__label"),
          div(inputElement),
          enterButton,
          div(h3("Connected : ", child.text <-- ws.isConnected)),
        ))
      case _ => div(
        div(h3("response from ws")),
        div(label(child.text <--ws.received.map(data => {
          Console.println(data)
//          data.user.userName
          data.toString
        }))),
        div(h3("--------------"))
      )
    }
  }
}
