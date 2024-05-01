package handler

import com.raquo.laminar.api.L.{Var, onClick}
import com.raquo.laminar.api.A.{EventStream, FetchStream}
import com.raquo.laminar.api.{enrichSource, eventPropToProcessor}
import com.raquo.laminar.modifiers.EventListener
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import model.{Action, Data, Room, User}
import org.scalajs.dom
import org.scalajs.dom.{Event, HTMLButtonElement, HTMLImageElement, HTMLInputElement, MouseEvent}
import domAction.DomAction
import factory.ElementFactory
import io.laminext.core.ResizeObserverBinders.-->
import io.laminext.fetch.upickle.{Fetch, FetchResponse}
import org.scalajs.dom.idb.EventTarget

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.Object.keys
import scala.util.{Failure, Success}

object ActionHandler {

  def clicActionEnterRoom(appContainer:  dom.Element,
                 ws: WebSocket[Data, Data],
                 inputElement: ReactiveHtmlElement[HTMLInputElement],
                 enterButton:  ReactiveHtmlElement[HTMLButtonElement],
                 userName: Var[String]):  EventListener[MouseEvent, Future[FetchResponse[String]]]  = {
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
            val initRoom: Room = null
            println("Success")
            userId.set(id.data)
            println(s"In callBack with ${userId.now()}")
            DomAction.renderDom(appContainer, ws, inputElement, enterButton, userName, userId, Var(initRoom))

            val newUser = User(s"${inputElement.ref.value}", userId.now(), Action[String, String]("None", "None"))
            val newDataState = Data(newUser, initRoom)
            ws.sendOne(newDataState)
          }
          case Failure(_)  => "???"
        }
      }
    }
  }

  def clicActionCard(ws: WebSocket[Data, Data],
                     user: User,
                     room: Var[Room]):  EventListener[MouseEvent, String]  = {
    onClick.map(mouseEvent => {
     val target = mouseEvent.currentTarget.asInstanceOf[HTMLImageElement]
      println(s"In clicActionCard for ${target.src}")
      val cardName: String = target.src.split("/").last.split("\\.").head

      val newUser = user.copy(action = Action[String, String](cardName, "selection_done"))
      val newUsers = room.now().users.filterNot(u => u.userId == user.userId).appended(newUser)
      val newDataState = Data(newUser, room.now().copy(users = newUsers))
      ws.sendOne(newDataState)
      mouseEvent.target.toString
    }) --> {
      _ => {
        println(s"In ws.sendOne for ${user}")
        //DomAction.renderDom(appContainer, ws, inputElement, enterButton, userName, userId)

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
