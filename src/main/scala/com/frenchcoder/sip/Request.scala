package com.frenchcoder.sip


class Method(name: String) {
  
}

case object Register extends Method("REGISTER")
case object Invite extends Method("INVITE")
case object Ack extends Method("ACK")
case object Cancel extends Method("CANCEL")
case object Bye extends Method("BYE")
case object Options extends Method("OPTIONS")


class Request[A](val method: Method, val headers: Headers, val body: A) {
  
}


