import com.raquo.laminar.api.L._
import com.raquo.laminar.modifiers.EventListener
import com.raquo.laminar.nodes._
import io.laminext.fetch.upickle.FetchResponse
import org.scalajs.dom
import org.scalajs.dom.window.location
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement, MouseEvent}
import main.scala.model.{Data, User}
import main.scala.model.ImplicitCodec.{dataRw, userRw}
import io.laminext.websocket.upickle._

import scala.concurrent.Future

object AgilePokerFrontEnd {

  private val userName: Var[String] = Var("")
  private val disabledEnter: Var[Boolean] = Var(true)

  def main(args: Array[String]): Unit = {

    println(s"args = $args")

    val roomIdPath: String = location.pathname.split("/").slice(1, 4).mkString("_")

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")

    val ws: WebSocket[Data, User] =
      WebSocket.url(s"ws://localhost:8080/connect/${roomIdPath}").json[Data, User].build()

    val enterButton: ReactiveHtmlElement[HTMLButtonElement] =
      builder.ElementBuilder.ButtonBuilder()
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
    val ca: EventListener[MouseEvent, Future[FetchResponse[String]]] = handler.ActionHandler.clicActionEnterRoom(appContainer, ws, inputElement, enterButton, userName, roomIdPath)
    ca.apply(enterButton)

    // this is how you render the appElement in the browser
    domAction.DomAction.renderDom(appContainer, ws, inputElement, enterButton, userName, Var(""))
  }
}

// 1) Run "fastOptJS" in sbt console to build js file
// 2) open index.html

// resync project in IDE = ~/.local/share/coursier/bin/mill mill.idea.GenIdea/idea
