package com.frenchcoder.sip.parser

import org.parboiled2._

object MessageParser {
  val reserved = CharPredicate(";/?:@&=+$,")
  val mark = CharPredicate("-_.!~*'()")
  val unreserved = CharPredicate.AlphaNum ++ mark
  val userUnreserved  =  CharPredicate("&=+$,;?/")
}

class MessageParser(val input: ParserInput) extends Parser with TelParser with IpAddressParser {
  import MessageParser._

  def escaped = rule { '%' ~ CharPredicate.HexDigit ~ CharPredicate.HexDigit }
  def userInfo = rule { ( user | telephoneSubscriber ) ~ optional(":" ~ password) ~ "@"}
  def user = rule { oneOrMore(unreserved | escaped | userUnreserved ) }
  def password = rule { oneOrMore(unreserved | escaped | CharPredicate("&=+$,")) }
  def method = rule { "REGISTER" | "INVITE" | "ACK" | "CANCEL" | "BYE" | "OPTIONS" }
  def port = rule { oneOrMore(CharPredicate.Digit) }
  def host = rule { domain | IpV4Address | IpV6Address }
  def hostAndPort = rule { host ~ optional(':' ~ port) }
  //def SipUri = rule { "sip:" ~ optional(userInfo) ~ hostAndPort ~ uriParameters ~ optional(headers)}
  //def SipsUri = rule { "sips:" ~ optional(userInfo) ~ hostAndPort ~ uriParameters ~ optional(headers)}
}


