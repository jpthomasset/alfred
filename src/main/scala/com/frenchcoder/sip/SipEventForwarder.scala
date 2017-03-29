package com.frenchcoder.sip

import akka.actor.ActorRef
import javax.sip._

class SipEventForwarder(actorRef: ActorRef) extends SipListener {

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
