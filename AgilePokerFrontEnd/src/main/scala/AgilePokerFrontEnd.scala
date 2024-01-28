import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes._
import com.raquo.laminar.modifiers._

import org.scalajs.dom
import io.laminext.websocket.circe._
import io.laminext.syntax.core._
import io.laminext.fetch.circe._
import io.circe._


import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement, MouseEvent}

object AgilePokerFrontEnd {

  case class Data(s: String)
  implicit val codeData: Codec[Data] = Codec.from(
    Decoder.decodeString.map(Data(_)),
    Encoder.encodeString.contramap(_.s),
  )

  private val roomId: Var[String] = Var("1234")
  private val userName: Var[String] = Var("")
  private val disabledEnter: Var[Boolean] = Var(true)

  def main(args: Array[String]): Unit = {

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")

    val ws: WebSocket[Data, Data] =
      WebSocket.url(s"ws://localhost:8080/connect/${roomId.now()}").json[Data, Data].build()

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
                 ws: WebSocket[Data, Data],
                 inputElement: ReactiveHtmlElement[HTMLInputElement],
                 enterButton:  ReactiveHtmlElement[HTMLButtonElement]):  EventListener[MouseEvent, Data]  = {
    onClick.map(_ => {
      userName.set(inputElement.ref.value)
      updateDom(appContainer, ws, inputElement, enterButton)
      Data(s"${roomId.now()}*${inputElement.ref.value}")
    }) --> ws.send
  }

  def updateDom(appContainer:  dom.Element,
                ws: WebSocket[Data, Data],
                inputElement: ReactiveHtmlElement[HTMLInputElement],
                enterButton:  ReactiveHtmlElement[HTMLButtonElement]): Unit = {
    render(appContainer, initElement(ws, inputElement, enterButton))
  }

  def initElement(ws: WebSocket[Data, Data],
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
        div(label(child.text <--ws.received.scanLeft("")(_ + _)))
      }
    }
  }
}


// 1) Run "fastOptJS" in sbt console to build js file
// 2) open index.html

// resync project in IDE = ~/.local/share/coursier/bin/mill mill.idea.GenIdea/idea
