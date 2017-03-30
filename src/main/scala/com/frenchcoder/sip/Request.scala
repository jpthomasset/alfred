package com.frenchcoder.sip

import gov.nist.javax.sip.message.SIPMessage
import javax.sip.{ message => jsm }
import scala.collection.JavaConverters._


class Method(val name: String)

case object Register extends Method("REGISTER")
case object Invite extends Method("INVITE")
case object Ack extends Method("ACK")
case object Cancel extends Method("CANCEL")
case object Bye extends Method("BYE")
case object Options extends Method("OPTIONS")


class Request[A](val method: Method, val headers: Headers, val body: A) {
  
}

object Request {
  def apply(request: jsm.Request): Request[_] = {
    val sipmessage = request.asInstanceOf[SIPMessage];
    val method = new Method(request.getMethod)
    val headers = Headers(sipmessage.getHeaders.asScala)

    headers.`Content-Length` match {
      case Some(l) if l > 0 => new Request(method, headers, new Content(sipmessage.getMessageContent))
      case Some(_) => new Request(method, headers, null)
      case None => new Request(method, headers, null)
    }
    
  }
}

