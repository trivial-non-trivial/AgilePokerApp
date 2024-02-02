package main.scala.model
import upickle.default._
object ImplicitCodec {

  implicit def dataRw: ReadWriter[Data] = macroRW
  implicit def userRw: ReadWriter[User] = macroRW
  implicit def roomRw: ReadWriter[Room] = macroRW

}
