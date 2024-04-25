import com.raquo.laminar.api.L._
import com.raquo.laminar.modifiers.EventListener
import com.raquo.laminar.nodes._
import factory.ElementFactory.{Button, Input}
import builder.ElementBuilder.{ButtonBuilder, CheckBoxBuilder, InputBuilder}
import factory.ElementFactory
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

    //println(s"args = ${args.mkString("Array(", ", ", ")")}")

    val roomIdPath: String = location.pathname.split("/").slice(1, 4).mkString("_")

    val appContainer: dom.Element = dom.document.querySelector("#appContainer")

    val ws: WebSocket[Data, User] =
      WebSocket.url(s"ws://localhost:8080/connect/${roomIdPath}").json[Data, User].build()

    val enterButton: Button =
      ButtonBuilder()
        .withLabel("Access room")
        .withRole("button")
        .withClass("button-3")
        // 'disabledEnter' is a Signal modified by a change on 'Input'
        // and bound to the button in the builder
        .withDisabledVar(disabledEnter)
        .build()

    def shouldActivate: (Input) => Boolean = {
      (input : Input) => input.ref.value.equals("")
    }

    val inputElement: Input =
      InputBuilder()
        .withClass("form__field")
        .withType("text")
        .withPredicat(shouldActivate)
        .withDisabledVar(disabledEnter)
        .build()

    val ca: EventListener[MouseEvent, Future[FetchResponse[String]]] =
      handler.ActionHandler.clicActionEnterRoom(appContainer, ws, inputElement, enterButton, userName, roomIdPath)
    ca.apply(enterButton)

    // this is how you render the appElement in the browser
    domAction.DomAction.renderDom(appContainer, ws, inputElement, enterButton, userName, Var(""))
  }
}
