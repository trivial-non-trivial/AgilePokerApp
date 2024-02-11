package handler

import com.raquo.laminar.api.L.{Var, onClick}
import com.raquo.laminar.api.A.{EventStream, FetchStream}
import com.raquo.laminar.api.{enrichSource, eventPropToProcessor}
import com.raquo.laminar.modifiers.EventListener
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import main.scala.model.{Data, User}
import org.scalajs.dom
import org.scalajs.dom.{HTMLButtonElement, HTMLInputElement, MouseEvent}
import domAction.DomAction
import io.laminext.core.ResizeObserverBinders.-->
import io.laminext.fetch.upickle.{Fetch, FetchResponse}

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}

object ActionHandler {

  def clicActionEnterRoom(appContainer:  dom.Element,
                 ws: WebSocket[Data, User],
                 inputElement: ReactiveHtmlElement[HTMLInputElement],
                 enterButton:  ReactiveHtmlElement[HTMLButtonElement],
                 userName: Var[String],
                 roomId: String):  EventListener[MouseEvent, Future[FetchResponse[String]]]  = {
    onClick.map(_ => {
      println(s"In ClicActionEnterRoom for ${inputElement.ref.value}")
      userName.set(inputElement.ref.value)
      Fetch.get(s"http://localhost:8080/uid/${inputElement.ref.value}")
        .headers(Map("Content-type" -> "text/plain; charset=utf-8",
                     "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"))
        .future
        .text()
      }) --> {
      responseText => {
        val userId: Var[String] = Var("init")
        println(s"In callBack for ${userName.now()}")
        responseText.onComplete {
          case Success(id) => {
            println("Success")
            userId.set(id.data)
            println(s"In callBack with ${userId.now()}")
            DomAction.renderDom(appContainer, ws, inputElement, enterButton, userName, userId)

            ws.sendOne(User(s"${roomId.split("_")(1)}_${inputElement.ref.value}", userId.now()))
          }
          case Failure(_)  => "???"
        }
      }
    }
  }

  def clickButtonToGetUid(userName: Var[String], userUid: Var[String]):  EventListener[MouseEvent, String] = {
    onClick.map(_ => {
      println("In ActionHandler")
      FetchStream.get(s"http://localhost:8080/uid/${userName.now()}").displayName
    }
    ) --> userUid
  }
}
