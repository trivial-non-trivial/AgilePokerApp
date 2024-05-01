package factory

import com.raquo.laminar.api.L._
import com.raquo.laminar.modifiers.EventListener
import com.raquo.laminar.nodes.ReactiveHtmlElement
import io.laminext.websocket.upickle.WebSocket
import model.{Data, Room, User}
import org.scalajs.dom.{Event, HTMLButtonElement, HTMLDivElement, HTMLImageElement, HTMLInputElement, MouseEvent, window}

object ElementFactory {

  type CheckBox = ReactiveHtmlElement[HTMLDivElement]
  type Button = ReactiveHtmlElement[HTMLButtonElement]
  type Image = ReactiveHtmlElement[HTMLImageElement]
  type Input = ReactiveHtmlElement[HTMLInputElement]

  def getCheckBox(label_ : String,
                  varBool: Var[Boolean] = Var(false),
                  cls_ : String = "checkbox-wrapper-14",
                  typ_ : String = "checkbox",
                  cls_s: String = "switch",
                  ws: WebSocket[Data, Data],
                  user: Var[User],
                  room: Var[Room]): CheckBox =
    div(cls := s"$cls_",
        input(typ := s"$typ_",
          cls := s"$cls_s",
          ws.received.map(es => es.room.show) --> varBool,
          checked <-- varBool,
          inContext{ a => onClick.map(event => {
            println(s"${user.now()} -> ${a.ref.checked}")
            ws.sendOne(Data(user.now(), room.now().copy(show = a.ref.checked)))
            a.ref.checked
          }) --> varBool}),
        label(s"$label_"))


  def getButton(label: String = "Click me !",
                cls_ : String = "button-3",
                role_ : String = "button",
                disabledVar: Var[Boolean] = Var(false)): Button =
    button(label,
      cls := cls_,
      role := role_,
      disabled <-- disabledVar
    )

  def getImage(src_ : String,
               cls_ : String = "img-1",
               v : User,
               card: Var[String] = Var(""),
               actionOnClick: EventListener[MouseEvent, String],
               ratio: Double
              ): Image =
    img(src := src_,
        idAttr := src_.split("/").last.split("\\.").head,
        cls := cls_,
        width := s"${200 * ratio}px",
        height := s"${300 * ratio}px",
        actionOnClick.eventProcessor --> card)

  def getInput(cls_ : String = "form__field",
               tpe_ : String = "text",
               predicat : Input => Boolean,
               signal : Var[Boolean] = Var(true)): Input = {
    input(
      cls := cls_,
      tpe := tpe_,
      // 'inContext' is here to introduce a side-effect on/by the element 'Input' :
      //   -> 'disableEnter' (a Signal) is modified if the 'value' state change
      inContext{ a => onChange.map(_ => predicat(a)) --> signal}
    )
  }
}
