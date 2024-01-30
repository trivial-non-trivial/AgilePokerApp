import com.raquo.laminar.Implicits
import io.circe.{Codec, Decoder, Encoder, Json, JsonObject}

object ImplicitCodec extends Implicits{

  implicit val codecData: Codec[User] = Codec.from(
    Decoder.decodeJsonObject.map(json => User(json("userName").toString, json("userId").toString)),
    Encoder.encodeJsonObject.contramap(user => {
      JsonObject.apply(
        "userName" -> Json.fromString(user.userName),
        "userId" -> Json.fromString(user.userId)
      )
    })
  )

  implicit val codecRoom: Codec[Room] = Codec.from(
    Decoder.decodeString.map(Room),
    Encoder.encodeString.contramap(_.s),
  )

}
