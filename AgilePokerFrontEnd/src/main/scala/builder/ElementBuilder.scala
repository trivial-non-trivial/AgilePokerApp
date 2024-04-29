package builder

import com.raquo.laminar.api.L.{Var, onClick}
import com.raquo.laminar.api.eventPropToProcessor
import com.raquo.laminar.modifiers.EventListener
import factory.ElementFactory
import factory.ElementFactory.Input
import io.laminext.websocket.upickle.WebSocket
import model.{Data, User}
import org.scalajs.dom.{Event, MouseEvent}

object ElementBuilder {

  class CheckBoxBuilder {
    private var cls: String = "checkbox-wrapper-14"
    private var varBool: Var[Boolean] = Var(false)
    private var typ: String = "checkbox"
    private var cls_i: String = "switch"
    private var label: String = "elementBuilder"
    private var ws: WebSocket[Data, User] = null
    private var user: Var[User] = null

    def withClass(cls : String): CheckBoxBuilder = {
      this.cls = cls
      this
    }

    def withVarBool(varBool: Var[Boolean]): CheckBoxBuilder = {
      this.varBool = varBool
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

    def withWs(ws: WebSocket[Data, User]): CheckBoxBuilder = {
      this.ws = ws
      this
    }

    def withUser(user: Var[User]): CheckBoxBuilder = {
      this.user = user
      this
    }

    def build(): ElementFactory.CheckBox =
      ElementFactory.getCheckBox(label, varBool, cls, typ, cls_i, ws, user)
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

  class InputBuilder {
    private var cls: String = "form__field"
    private var tpe: String = "text"
    private var predicat : Input => Boolean = _ => true
    private var disabledVar: Var[Boolean] = Var(false)

    def withClass(cls: String): InputBuilder = {
      this.cls = cls
      this
    }

    def withType(tpe: String): InputBuilder = {
      this.tpe = tpe
      this
    }

    def withDisabledVar(disabledVar: Var[Boolean]): InputBuilder = {
      this.disabledVar = disabledVar
      this
    }
    def withPredicat(predicat: Input => Boolean): InputBuilder = {
      this.predicat = predicat
      this
    }

    def build(): ElementFactory.Input =
      ElementFactory.getInput(cls, tpe, predicat, disabledVar)
  }

  object InputBuilder {
    def apply(): InputBuilder = new InputBuilder
  }

  class ImageBuilder {
    private var cls: String = "playedCard"
    private var src = s"cards/card_v1_back.png"
    private var ratio = 1.0
    private var v : User = null
    private var card: Var[String] = Var("")
    var actionOnClick: EventListener[MouseEvent, String] =
      new EventListener[MouseEvent, String](onClick.map(_ => ""), _ => ())

    def withClass(cls: String): ImageBuilder = {
      this.cls = cls
      this
    }
    def withSrc(src: String): ImageBuilder = {
      this.src = src
      this
    }
    def withRatio(ratio: Double): ImageBuilder = {
      this.ratio = ratio
      this
    }
    def withUser(v: User): ImageBuilder = {
      this.v = v
      this
    }
    def withCard(card: Var[String]): ImageBuilder = {
      this.card = card
      this
    }
    def withActionOnClick(actionOnClick: EventListener[MouseEvent, String]): ImageBuilder = {
      this.actionOnClick = actionOnClick
      this
    }

    def withActionOnClick2(actionOnClick: EventListener[MouseEvent, String]): ImageBuilder = {
      this.actionOnClick = actionOnClick
      this
    }


    def build(): ElementFactory.Image =
      ElementFactory.getImage(src, cls, v, card, actionOnClick, ratio)

  }

  object ImageBuilder {
    def apply(): ImageBuilder = new ImageBuilder
  }
}
