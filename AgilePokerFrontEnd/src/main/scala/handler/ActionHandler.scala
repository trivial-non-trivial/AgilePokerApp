package handler

import com.raquo.laminar.api.L.{Var, onClick}
import com.raquo.laminar.api.eventPropToProcessor
import com.raquo.laminar.modifiers.EventListener
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import main.scala.model.{Data, User}
import org.scalajs.dom
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement, MouseEvent}
import domAction.DomAction

object ActionHandler {

  def clicActionEnterRoom(appContainer:  dom.Element,
                 ws: WebSocket[Data, User],
                 inputElement: ReactiveHtmlElement[HTMLInputElement],
                 enterButton:  ReactiveHtmlElement[HTMLButtonElement],
                 userName: Var[String],
                 roomId: Var[String]):  EventListener[MouseEvent, User]  = {
    onClick.map(_ => {
      userName.set(inputElement.ref.value)
      DomAction.renderDom(appContainer, ws, inputElement, enterButton, userName)
      User(s"${roomId.now()}*${inputElement.ref.value}", "UID_00002")
    }) --> ws.send
  }

}
