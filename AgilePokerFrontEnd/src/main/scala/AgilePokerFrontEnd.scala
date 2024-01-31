import ImplicitCodec.{codecData}
import com.raquo.laminar.api.L._
import com.raquo.laminar.modifiers.EventListener
import com.raquo.laminar.nodes._
import org.scalajs.dom
import io.laminext.websocket.circe._
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement, MouseEvent}

object AgilePokerFrontEnd {

  private val roomId: Var[String] = Var("1234")
  private val userName: Var[String] = Var("")
  private val disabledEnter: Var[Boolean] = Var(true)

  def main(args: Array[String]): Unit = {

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")

    val ws: WebSocket[Data, Data] =
      WebSocket.url(s"ws://localhost:8080/connect/${roomId.now()}").json[Data, Data].build()

    val enterButton: ReactiveHtmlElement[HTMLButtonElement] =
      ElementBuilder.ButtonBuilder()
        .withLabel("Access room")
        .withRole("button")
        .withClass("button-3")
        .withDisabledVar(disabledEnter)
        .build()

    val inputElement: ReactiveHtmlElement[HTMLInputElement] =
      input(cls := "form__field",
        tpe := "text",
        inContext {
          thisNode => onChange.map(_ => thisNode.ref.value == "") --> disabledEnter
        }
      )
    val ca: EventListener[MouseEvent, Data] = ActionHandler.clicActionEnterRoom(appContainer, ws, inputElement, enterButton, userName, roomId)
    ca.apply(enterButton)

    // this is how you render the appElement in the browser
    DomAction.renderDom(appContainer, ws, inputElement, enterButton, userName)
  }
}

// 1) Run "fastOptJS" in sbt console to build js file
// 2) open index.html

// resync project in IDE = ~/.local/share/coursier/bin/mill mill.idea.GenIdea/idea
