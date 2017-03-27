package com.frenchcoder.alfred.sip

case class Status(status: Int)

object Status {
  val Trying = Status(100)
  val Ringing = Status(180)
  val Ok = Status(200)
  val Unauthorized = Status(401)
}
