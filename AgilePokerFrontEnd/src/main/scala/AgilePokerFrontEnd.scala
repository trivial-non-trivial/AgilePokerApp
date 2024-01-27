import com.raquo.laminar.api.L._
import org.scalajs.dom
import io.laminext.websocket._

object AgilePokerFrontEnd {

  private var state: Signal[Boolean] = Signal.fromValue(false)
  private val value: Var[String] = Var("")
  private val content: Var[String] = Var("")

  def main(args: Array[String]): Unit = {

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")

    val diffBus = new EventBus[Unit]
    val ws: WebSocket[String, String] =
      WebSocket.url("ws://localhost:8080/connect/haoyi").string.build(managed = true)

    val inputElement = input(cls := "form__field", tpe := "text")
    val enterButton = button("Access room",
      cls := "button-3",
      role := "button",
      onClick.map(_ => inputElement.ref.value) --> ws.send
    )

    val appElement: Div = div(
      h2("Register in Agile Poker", cls := "h2-1"),
      div(cls := "form__group field",
        ws.connect,
        label("Name", cls := "form__label"),
        div(inputElement),
        enterButton,
        div(h3(child.text <-- ws.isConnected)),
        div(h3(child.text <-- content.signal)),
        div(h3(child.text <-- ws.received))
      )
    )

    // this is how you render the appElement in the browser
    render(appContainer, appElement)
  }
}


// 1) Run "fastOptJS" in sbt console to build js file
// 2) open index.html
