package factory

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import main.scala.model.User
import org.scalajs.dom.{Event, HTMLButtonElement, HTMLDivElement, HTMLImageElement, HTMLInputElement, window}

object ElementFactory {

  type CheckBox = ReactiveHtmlElement[HTMLDivElement]
  type Button = ReactiveHtmlElement[HTMLButtonElement]
  type Image = ReactiveHtmlElement[HTMLImageElement]
  type Input = ReactiveHtmlElement[HTMLInputElement]

  def getCheckBox(label_ : String,
                  cls_ : String = "checkbox-wrapper-14",
                  typ_ : String = "checkbox",
                  cls_s: String = "switch"): CheckBox =
    div(cls := s"$cls_",
        input(typ := s"$typ_", cls := s"$cls_s"),
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
               card: Var[String] = Var("")
              ): Image =
    img(src := src_,
        cls := cls_,
        width := s"${200*75/100}px",
        height := s"${300*75/100}px",
        onClick.map(event => {
          println(v + " clicked")
          "out"
        }) --> card)

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
