import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.{HTMLButtonElement, HTMLDivElement}

object ElementFactory {

  type CheckBox = ReactiveHtmlElement[HTMLDivElement]
  type Button = ReactiveHtmlElement[HTMLButtonElement]

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
                disabledVar: Var[Boolean] = Var(false)): ReactiveHtmlElement[HTMLButtonElement] =
    button(label,
      cls := cls_,
      role := role_,
      disabled <-- disabledVar
    )
}
