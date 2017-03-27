package com.frenchcoder.alfred.sip


case class Method(method: String)

object Method {
  val Register = Method("REGISTER")
  val Invite = Method("INVITE")
  val Ack = Method("ACK")
  val Cancel = Method("CANCEL")
  val Bye = Method("BYE")
  val Options = Method("OPTIONS")
}

class Request(val method: Method) {
  def send : Unit = {

  }
}

import Method._

class Register(to: String) extends Request(Register) {
  
}

class Invite(to: String) extends Request(Invite) {
  
}
