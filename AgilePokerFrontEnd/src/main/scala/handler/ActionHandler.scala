package handler

import com.raquo.laminar.api.L._
import com.raquo.laminar.api.eventPropToProcessor
import com.raquo.laminar.modifiers.EventListener
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import main.scala.model.{Data, User}
import org.scalajs.dom
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement, MouseEvent}
import domAction.DomAction
import io.laminext.fetch.{Fetch, FetchResponse}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}

object ActionHandler {

  val (responsesStream, responseReceived) = EventStream.withCallback[FetchResponse[String]]

  def clicActionEnterRoom(appContainer:  dom.Element,
                 ws: WebSocket[Data, User],
                 inputElement: ReactiveHtmlElement[HTMLInputElement],
                 enterButton:  ReactiveHtmlElement[HTMLButtonElement],
                 userName: Var[String],
                 roomId: String):  EventListener[MouseEvent, User]  = {
    onClick.map(_ => {
      userName.set(inputElement.ref.value)
      val userId: Var[String] = Var("!!!!")
      Fetch.get(s"http://localhost:8080/uid/${userName.now()}").text.recoverToTry.map {
        case Success(uid) => uid.data
        case Failure(_) => "????"
      } --> userId
      DomAction.renderDom(appContainer, ws, inputElement, enterButton, userName)
      User(s"${inputElement.ref.value}", userId.now())
    }) --> ws.send
  }

}
