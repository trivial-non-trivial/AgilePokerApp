package model
import upickle.default._
object ImplicitCodec {

  implicit val dataRw:   ReadWriter[Data] = macroRW
  implicit val userRw:   ReadWriter[User] = macroRW
  implicit val roomRw:   ReadWriter[Room] = macroRW
  implicit val actionRw: ReadWriter[Action[String, String]] = macroRW


}
