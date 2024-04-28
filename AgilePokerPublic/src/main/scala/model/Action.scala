package model

case class Action[T, R](input: T, result: R)
