import com.raquo.laminar.Implicits
import io.circe.{Codec, Decoder, Encoder, Json, JsonObject}
import main.scala.model.{User, Data, Room}

object ImplicitCodec extends Implicits{

  implicit val codecUser: Codec[User] = Codec.from(
    Decoder.decodeJsonObject.map(json => User(json("userName").toString,
      json("userId").toString)),
    Encoder.encodeJsonObject.contramap(user => {
      JsonObject.apply(
        "userName" -> Json.fromString(user.userName),
        "userId"   -> Json.fromString(user.userId)
      )
    })
  )

  implicit val codecRoom: Codec[Room] = Codec.from(
    Decoder.decodeJsonObject.map(json => Room(json("users").map(_.as[User] match {
      case Right(u: User) => u
      case _              => User("--", "")
    }).toSeq)),
    Encoder.encodeJsonObject.contramap(room => {
      JsonObject.apply(
        "users" -> Json.fromValues(room.users.map(codecUser.apply))
      )
    }
    ))

  implicit val codecData: Codec[Data] = Codec.from(
    Decoder.decodeJsonObject.map(json =>
      Data(json("user").map(_.as[User] match {
        case Right(u: User) => u
        case _              => User("--", "")
      }).get,
        json("room").map(_.as[Room] match {
          case Right(u: Room) => u
          case _              => Room(Seq.empty)
        }).get)),
    Encoder.encodeJsonObject.contramap(data => {
      JsonObject.apply(
        "user" -> codecUser.apply(data.user),
        "room" -> codecRoom.apply(data.room)
      )
    }
    ))
}
