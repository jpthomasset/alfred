package com.frenchcoder.sip

import gov.nist.javax.sip.message.SIPMessage
import javax.sip.{ message => jsm }


case class Method(name: String)

object Method {
  val Register = Method("REGISTER")
  val Invite = Method("INVITE")
  val Ack = Method("ACK")
  val Cancel = Method("CANCEL")
  val Bye = Method("BYE")
  val Options = Method("OPTIONS")
}

class Request[A](val method: Method, headers: Headers, body: A) extends Message[A](headers, body) {
  
}

object Request {
  def apply(request: jsm.Request): Request[_] = 
    Message.build(request.asInstanceOf[SIPMessage], (h, c) => new Request(Method(request.getMethod), h, c))

  def apply(method: Method): Request[_] = new Request(method, new Headers(Seq.empty), EmptyContent)
}



