package com.frenchcoder.sip

import javax.sip._
import javax.sip.address.AddressFactory
import javax.sip.header.HeaderFactory
import javax.sip.message.{ MessageFactory }

case class SipContext(ip: String, port: Int, stack: SipStack, provider: SipProvider, addressFactory: AddressFactory, messageFactory: MessageFactory, headerFactory: HeaderFactory)

class SipClient(val sip: SipContext) extends SipListener {
  val cseqgen = new SequenceGenerator()
  val tag: Int = 4321

  def processRequest(event: RequestEvent): Unit = {

  }

  def processResponse(event: ResponseEvent): Unit = {
    println(event.getResponse)
  }

  def processDialogTerminated(event: DialogTerminatedEvent): Unit = {

  }

  def processIOException(event: IOExceptionEvent): Unit = {
    println(event)
  }

  def processTimeout(event: TimeoutEvent): Unit = {

  }

  def processTransactionTerminated(event: TransactionTerminatedEvent): Unit = {

  }
}
