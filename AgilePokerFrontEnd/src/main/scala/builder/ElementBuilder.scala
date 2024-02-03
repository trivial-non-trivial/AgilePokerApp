package builder

import com.raquo.laminar.api.L.Var
import factory.ElementFactory

object ElementBuilder {

  class CheckBoxBuilder {
    private var cls: String = "checkbox-wrapper-14"
    private var typ: String = "checkbox"
    private var cls_i: String = "switch"
    private var label: String = "elementBuilder"

    def withClass(cls : String): CheckBoxBuilder = {
      this.cls = cls
      this
    }

    def withType(typ : String): CheckBoxBuilder = {
      this.typ = typ
      this
    }

    def withLabel(label : String): CheckBoxBuilder = {
      this.label = label
      this
    }

    def build(): ElementFactory.CheckBox =
      ElementFactory.getCheckBox(label, cls, typ, cls_i)
  }

  object CheckBoxBuilder {
      def apply(): CheckBoxBuilder = new CheckBoxBuilder
  }

  class ButtonBuilder {
    private var cls: String = "button-3"
    private var role: String = "button"
    private var label: String = "Click me !"
    private var disabledVar: Var[Boolean] = Var(false)

    def withClass(cls : String): ButtonBuilder = {
      this.cls = cls
      this
    }

    def withRole(role : String): ButtonBuilder = {
      this.role = role
      this
    }

    def withLabel(label : String): ButtonBuilder = {
      this.label = label
      this
    }

    def withDisabledVar(disabledVar: Var[Boolean]): ButtonBuilder = {
      this.disabledVar = disabledVar
      this
    }

    def build(): ElementFactory.Button =
      ElementFactory.getButton(label, cls, role, disabledVar)
  }

  object ButtonBuilder {
    def apply(): ButtonBuilder = new ButtonBuilder
  }
}
