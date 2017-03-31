package com.frenchcoder.sip

import gov.nist.javax.sip.message.SIPMessage
import scala.collection.JavaConverters._

abstract class Message[A](val headers: Headers, val body: A)

object Message {

  def build[A](message: SIPMessage, builder: (Headers, AbstractContent) => A): A = {
    val headers = Headers(message.getHeaders.asScala)

    val content = headers.`Content-Length` match {
      case Some(l) if l > 0 => Content(message.getMessageContent, headers.`Content-Type`.getOrElse("text/plain"))
      case Some(_) => EmptyContent
      case None => EmptyContent
    }

    builder(headers, content)    
  }
}
