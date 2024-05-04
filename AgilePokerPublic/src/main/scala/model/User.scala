package model

case class User(userName: String, userId: String, action: Action[String, String], var connexionClosed: Boolean = false)