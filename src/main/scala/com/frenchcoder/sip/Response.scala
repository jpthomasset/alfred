package com.frenchcoder.sip

import gov.nist.javax.sip.message.SIPMessage
import javax.sip.{ message => jsm }


case class Status(status: Int)

object Status {
  val Trying = Status(100)
  val Ringing = Status(180)
  val Ok = Status(200)
  val Unauthorized = Status(401)
}

class Response[A](val status: Status, headers: Headers, body: A) extends Message[A](headers, body) {
  
}

object Response {
  def apply(response: jsm.Response): Response[_] = 
    Message.build(response.asInstanceOf[SIPMessage], (h, c) => new Response(Status(response.getStatusCode), h, c))
}
