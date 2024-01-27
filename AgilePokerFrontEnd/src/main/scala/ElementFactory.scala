import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLDivElement

object ElementFactory {

  type CheckBox = ReactiveHtmlElement[HTMLDivElement]

  def getCheckBox(label_ : String,
                  cls_ : String = "checkbox-wrapper-14",
                  typ_ : String = "checkbox",
                  cls_s: String = "switch"): CheckBox =
    div(cls := s"$cls_",
        input(typ := s"$typ_", cls := s"$cls_s"),
        label(s"$label_"))
}
