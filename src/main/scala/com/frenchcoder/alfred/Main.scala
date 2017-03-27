package com.frenchcoder.alfred

import java.util.Properties
import javax.sip._
import javax.sip.address.AddressFactory
import javax.sip.header.HeaderFactory
import javax.sip.message.{ MessageFactory, Request }
import scala.concurrent.Future
import scala.io.StdIn
import scala.util.{ Failure, Success, Try }
import collection.JavaConverters._


object Main {
  def main(args: Array[String]): Unit = {
    val client = SipClient.create("192.168.1.18", 5070)
    client match {
      case Success(c) => println(s"Ok $c")
      case Failure(t) => println(s"Err: $t")
    }
   // client.foreach(_.register("sb8jzru5", "192.168.1.200"))

    println(s"\u001B[32mStarting SampleClient, Ctrl+D to stop and go back to the console...\u001B[0m")
    while(StdIn.readLine() != null) {}
    System.exit(0)
  }
}

class CSeq {
  var seq = 0l
  def next : Long = {
    seq = seq + 1
    seq
  }
}

case class SipContext(ip: String, port: Int, stack: SipStack, provider: SipProvider, addressFactory: AddressFactory, messageFactory: MessageFactory, headerFactory: HeaderFactory)

class SipClient(val sip: SipContext) extends SipListener {
  val cseq = new CSeq
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

// case class SipAccount(username: String, password: String)

// trait SipRequest {
//   implicit def sipClient: SipClient
//   def action: String
//   def to: String
//   def from: String
//   def send(f: => Unit): Unit = {
//     val callId = sipClient.sip.provider.getNewCallId();
//     val cseqHeader = sipClient.sip.headerFactory.createCSeqHeader(sipClient.cseq.next, action)
//     val toAddress = sipClient.sip.addressFactory.createAddress(to)
//     val fromAddress = sipClient.sip.addressFactory.createAddress(from)
//     val fromH = sipClient.sip.headerFactory.createFromHeader(fromAddress, sipClient.tag.toString)
//     val toH = sipClient.sip.headerFactory.createToHeader(toAddress, null)
//     val maxFwd = sipClient.sip.headerFactory.createMaxForwardsHeader(70)
//     val via = sipClient.sip.headerFactory.createViaHeader(sipClient.sip.ip, sipClient.sip.port, "udp", null)
//     val contact = sipClient.sip.headerFactory.createContactHeader(fromAddress)
    
//     val rq = sipClient.sip.messageFactory.createRequest(toAddress.getURI, Request.REGISTER, callId, cseqHeader, fromH, toH, List(via).asJava, maxFwd)
//     rq.addHeader(contact)
//     sipClient.sip.provider.sendRequest(rq)

//   }
// }


object SipClient {
  def create(localAddress: String, port: Int): Try[SipClient] = {
    val sipFactory = SipFactory.getInstance();
    sipFactory.setPathName("gov.nist");
    val properties = new Properties();
    properties.setProperty("javax.sip.STACK_NAME", "stack");
    for {
      stack <- Try(sipFactory.createSipStack(properties))
      header <- Try(sipFactory.createHeaderFactory())
      address <- Try(sipFactory.createAddressFactory())
      message <- Try(sipFactory.createMessageFactory())
      lp <- Try(stack.createListeningPoint(localAddress, port, "udp"))
      provider <- Try(stack.createSipProvider(lp))
      clt <- Try(new SipClient(SipContext(localAddress, port, stack, provider, address, message, header)))
      _ <- Try(provider.addSipListener(clt))
    } yield clt
  }
}
