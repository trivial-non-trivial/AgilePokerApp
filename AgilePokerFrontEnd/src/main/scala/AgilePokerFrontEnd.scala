import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes._
import com.raquo.laminar.modifiers._
import org.scalajs.dom
import io.laminext.websocket._
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement, MouseEvent}

object AgilePokerFrontEnd {

  private val roomId: Var[String] = Var("1234")
  private val userName: Var[String] = Var("")
  private val disabledEnter: Var[Boolean] = Var(true)

  def main(args: Array[String]): Unit = {

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")

    val ws: WebSocket[String, String] =
      WebSocket.url(s"ws://localhost:8080/connect/${roomId.now()}").string.build(managed = true)

    val enterButton: ReactiveHtmlElement[HTMLButtonElement] = button("Access room",
      cls := "button-3",
      role := "button",
      disabled <-- disabledEnter
    )

    val inputElement: ReactiveHtmlElement[HTMLInputElement] =
      input(cls := "form__field",
        tpe := "text",
        inContext{
          thisNode => onChange.map(_ => thisNode.ref.value == "") --> disabledEnter}
      )
    val ca = clicAction(appContainer, ws, inputElement, enterButton)
    ca.apply(enterButton)

    // this is how you render the appElement in the browser
    render(appContainer, initElement(ws, inputElement, enterButton))
  }

  def clicAction(appContainer:  dom.Element,
                 ws: WebSocket[String, String],
                 inputElement: ReactiveHtmlElement[HTMLInputElement],
                 enterButton:  ReactiveHtmlElement[HTMLButtonElement]):  EventListener[MouseEvent, String]  = {
    onClick.map(_ => {
      userName.set(inputElement.ref.value)
      updateDom(appContainer, ws, inputElement, enterButton)
      s"${roomId.now()}*${inputElement.ref.value}"
    }) --> ws.send
  }

  def updateDom(appContainer:  dom.Element,
                ws: WebSocket[String, String],
                inputElement: ReactiveHtmlElement[HTMLInputElement],
                enterButton:  ReactiveHtmlElement[HTMLButtonElement]): Unit = {
    render(appContainer, initElement(ws, inputElement, enterButton))
  }

  def initElement(ws: WebSocket[String, String],
                  inputElement: ReactiveHtmlElement[HTMLInputElement],
                  enterButton: ReactiveHtmlElement[HTMLButtonElement]): Div = {

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
      case _ => {
        div(label(child.text <--ws.received))
      }
    }
  }
}


// 1) Run "fastOptJS" in sbt console to build js file
// 2) open index.html
