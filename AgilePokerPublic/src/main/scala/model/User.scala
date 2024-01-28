package model

import io.circe._

case class User(userName: String, userId: String)

object User {
  implicit val codeData: Codec[User] = Codec.from(
    Decoder.decodeJsonObject.map(json => User(json("userName").toString, json("userId").toString)),
    Encoder.encodeString.contramap(_.userName),
  )
}