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
}
