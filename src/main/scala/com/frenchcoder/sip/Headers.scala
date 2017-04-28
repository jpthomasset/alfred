package com.frenchcoder.sip

import gov.nist.javax.sip.{header => gsh}
import scala.reflect.ClassTag

class Headers(val headers: Seq[(String, String)]) {

  def getHeaders(name: String): Seq[String] =
    headers.filter { case (n, v) => n.compareToIgnoreCase(name) == 0 }.map(_._2)

  def getHeader(name: String): Option[String] =
    getHeaders(name).headOption

  def apply: String => Seq[String] = getHeaders

  def `To` = getHeader(Headers.`To`)
  def `From` = getHeader(Headers.`From`)
  def `CSeq` = getHeader(Headers.`CSeq`)
  def `Call-ID` = getHeader(Headers.`Call-ID`)
  def `Max-Forwards` = getHeader(Headers.`Max-Forwards`)
  def `Via` = getHeaders(Headers.`Via`)
  def `Content-Length` = getHeader(Headers.`Content-Length`).map(_.toInt)
  def `Content-Type` = getHeader(Headers.`Content-Type`)
  def `WWW-Authenticate` = getHeader(Headers.`WWW-Authenticate`)

}

object Headers {
  def apply(headers: Iterator[gsh.SIPHeader]) : Headers = {
    new Headers(headers.map(headerToSeq).toSeq)
  }

  def headerToSeq(header: gsh.SIPHeader): (String, String) = {
    (header.getName, header.getValue)
  }


  val `To` = "To"
  val `From` = "From"
  val `CSeq` = "CSeq"
  val `Call-ID` = "Call-ID"
  val `Max-Forwards` = "Max-Forwards"
  val `Via` = "Via"
  val `Content-Length` = "Content-Length"
  val `Content-Type` = "Content-Type"
  val `WWW-Authenticate` = "WWW-Authenticate"
}


trait Header {
  def name: String
  def value: String

}

case class GenericHeader (name: String, value: String) extends Header

case class CallId(callId: String) extends Header {
  val name = "Call-Id"
  val value = callId
}

case class CSeq(id: Long) extends Header {
  val name = "CSeq"
  val value = id.toString
}

abstract class HeaderModel[T: ClassTag] extends Header {

  private def nameFromModel(clazz: Class[T]): String = {
    //val n = clazz.getName
    ""
  }
}
