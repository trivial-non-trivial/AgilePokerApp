package src.main.scala.service

import model.{Data, Room, RoomState, User}

import scala.collection.mutable

object UserService {

  def repopUser(data: Data, room: Room, states: mutable.Map[String, RoomState]): User = {
    if(data.user.action.input == "None"
      && states.apply(room.roomId).room.users.map(_.userId).contains(data.user.userId)) {
        states.apply(room.roomId).room.users
          .find(_.userId == data.user.userId)
          .map(r => r.copy(connexionClosed = false))
          .get
    }
    else data.user
  }
}
