package com.frenchcoder.sip

import akka.actor.Actor
import javax.sip._
import javax.sip.address.AddressFactory
import javax.sip.header.HeaderFactory
import javax.sip.message.{ MessageFactory }

case class SipContext(ip: String, port: Int, stack: SipStack, provider: SipProvider, addressFactory: AddressFactory, messageFactory: MessageFactory, headerFactory: HeaderFactory)

class SipClient(val sip: SipContext, val account: String, val displayName: Option[String] = None) extends Actor {
  val cseqgen = new SequenceGenerator()
  val tag: Int = 4321

  def receive = Actor.emptyBehavior

  def send[A](request: Request[A]) = {
    val callId = sip.provider.getNewCallId();
    val cseqHeader = sip.headerFactory.createCSeqHeader(cseqgen.next, request.method.name)
    val maxFwd = sip.headerFactory.createMaxForwardsHeader(70)
    val via = sip.headerFactory.createViaHeader(sip.ip, sip.port, "udp", null)

    //val toAddress = sip.addressFactory.createAddress(to)
//    val fromAddress = sip.addressFactory.createAddress(from)
//     val fromH = sipClient.sip.headerFactory.createFromHeader(fromAddress, sipClient.tag.toString)
//     val toH = sipClient.sip.headerFactory.createToHeader(toAddress, null)

//     val via = sipClient.sip.headerFactory.createViaHeader(sipClient.sip.ip, sipClient.sip.port, "udp", null)
//     val contact = sipClient.sip.headerFactory.createContactHeader(fromAddress)
    
//     val rq = sipClient.sip.messageFactory.createRequest(toAddress.getURI, Request.REGISTER, callId, cseqHeader, fromH, toH, List(via).asJava, maxFwd)
//     rq.addHeader(contact)
//     sipClient.sip.provider.sendRequest(rq)
  }
}
