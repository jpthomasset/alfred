package com.frenchcoder.alfred

import java.util.Properties
import javax.sip._
import javax.sip.address.AddressFactory
import javax.sip.header.HeaderFactory
import javax.sip.message.MessageFactory
import scala.io.StdIn
import scala.util.Try


object Main {
  def main(args: Array[String]): Unit = {
    val client = SipClient.create("10.49.0.1", 5070)

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

class SipClient(sip: SipContext) extends SipListener {
  val cseq = new CSeq
  val tag: Int = 4321

  def register(username: String, server: String) = {
    val callId = sip.provider.getNewCallId();
    val header = sip.headerFactory.createCSeqHeader(cseq.next, "REGISTER")
    val address = sip.addressFactory.createAddress(s"sip:$username@$server")
    val from = sip.headerFactory.createFromHeader(address, tag.toString)
    val to = sip.headerFactory.createToHeader(address, null)
    val maxFwd = sip.headerFactory.createMaxForwardsHeader(70)
    val via = sip.headerFactory.createViaHeader(sip.ip, sip.port, "udp", null)
    val contactAddress = sip.addressFactory.createAddress(s"sip:$username@${sip.ip}")
    val contact = sip.headerFactory.createContactHeader(contactAddress)
    val exp = sip.headerFactory.createExpiresHeader(120)

    val rq = sip.messageFactory.createRequest(s"REGISTER sip:$server SIP/2.0\r\n\r\n")
  }

  def processRequest(event: RequestEvent): Unit = {

  }

  def processResponse(event: ResponseEvent): Unit = {
    println(event)
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

object SipClient {
  def create(localAddress: String, port: Int): Try[SipClient] = {
    val sipFactory = SipFactory.getInstance();
    sipFactory.setPathName("gov.nist");
    val properties = new Properties();
    properties.setProperty("javax.sip.STACK_NAME", "SipClient");
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
