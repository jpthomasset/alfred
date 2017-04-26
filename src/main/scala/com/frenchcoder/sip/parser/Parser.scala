package com.frenchcoder.sip.parser

import com.frenchcoder.sip._
import org.parboiled2._

object MessageParser {
  val reserved = CharPredicate(";/?:@&=+$,")
  val mark = CharPredicate("-_.!~*'()")
  val unreserved = CharPredicate.AlphaNum ++ mark
  val userUnreserved  =  CharPredicate("&=+$,;?/")
  val hnvUnreserved  =  CharPredicate("[]/?:+$")
  val SP = CharPredicate(' ')
  val WSP = CharPredicate(" \t")
  val UTF8CONT = CharPredicate('\u0080' to '\u00BF')
  val wordChar = CharPredicate.AlphaNum ++ CharPredicate("-.!%*_+`'~()<>:\\\"/[]?{}")
}

class MessageParser(val input: ParserInput) extends Parser with TelParser with IpAddressParser {
  import MessageParser._

  def CRLF = rule { "\r\n" }
  def LWS = rule { optional(zeroOrMore(WSP) ~ CRLF) ~ oneOrMore(WSP) }
  def SWS = rule { optional(LWS) }
  def HCOLON = rule { zeroOrMore(WSP) ~ ":" ~ SWS }
  def UTF8NONASCII = rule {
    (CharPredicate('\u00C0' to '\u00DF') ~ UTF8CONT) |
    (CharPredicate('\u00E0' to '\u00EF') ~ 2.times(UTF8CONT)) |
    (CharPredicate('\u00F0' to '\u00F7') ~ 3.times(UTF8CONT)) |
    (CharPredicate('\u00F8' to '\u00FB') ~ 4.times(UTF8CONT)) |
    (CharPredicate('\u00FC' to '\u00FD') ~ 5.times(UTF8CONT))
  }
  def TEXTUTF8char = rule { CharPredicate('\u0021' to '\u007E') | UTF8NONASCII }
  def EQUAL = rule { SWS ~ "=" ~ SWS }

  def quotedPair = rule { '\\' ~ (CharPredicate('\u0000' to '\u0009') | CharPredicate('\u000B' to '\u000C') | CharPredicate('\u000E' to '\u007F')) }
  def qdtext = rule { LWS | CharPredicate('\u0021') | CharPredicate('\u0023' to '\u005B') | CharPredicate('\u005D' to '\u007E') | UTF8NONASCII }

  def word = rule { oneOrMore(wordChar) }

  def escaped = rule { '%' ~ CharPredicate.HexDigit ~ CharPredicate.HexDigit }
  def userInfo = rule { ( user | telephoneSubscriber ) ~ optional(":" ~ password) ~ "@"}
  def user = rule { oneOrMore(unreserved | escaped | userUnreserved ) }
  def password = rule { oneOrMore(unreserved | escaped | CharPredicate("&=+$,")) }
  def method = rule { "REGISTER" | "INVITE" | "ACK" | "CANCEL" | "BYE" | "OPTIONS" }
  def port = rule { oneOrMore(CharPredicate.Digit) }
  def host = rule { domain | IpV4Address | IpV6Address }
  def hostAndPort = rule { host ~ optional(':' ~ port) }
  def token = rule { oneOrMore(CharPredicate.AlphaNum | CharPredicate("-.!%*_+`'~")) }
  def transportParam = rule { "transport=" ~ ("udp" | "tcp" | "sctp" | "tls" | token) }
  def userParam = rule { "user=" ~ ( "phone" | "ip" | token) }
  def methodParam = rule { "method=" ~ method }
  def ttl = rule { (1 to 3).times(CharPredicate.Digit) }
  def ttlParam = rule { "ttl=" ~ ttl }
  def maddrParam  = rule { "maddr=" ~ host }
  def param  =  rule { CharPredicate("[]/:&+$") | unreserved | escaped }
  def paramNameValue = rule { oneOrMore(param) }
  def otherParam = rule { paramNameValue ~ optional('=' ~ paramNameValue) }
  def hname = rule { oneOrMore(hnvUnreserved | unreserved | escaped) }
  def hvalue = rule { zeroOrMore(hnvUnreserved | unreserved | escaped) }
  def header = rule { hname ~ "=" ~ hvalue }
  def headers = rule { "?" ~ header ~ zeroOrMore("&" ~ header) }
  def uriParameter = rule { transportParam | userParam | methodParam | ttlParam | maddrParam | "lr" | otherParam }
  def uriParameters = rule { zeroOrMore(';' ~ uriParameter) }

  def SipUri = rule { "sip:" ~ optional(userInfo) ~ hostAndPort ~ uriParameters ~ optional(headers)}
  def SipsUri = rule { "sips:" ~ optional(userInfo) ~ hostAndPort ~ uriParameters ~ optional(headers)}
  def SipVersion = rule { "SIP/" ~ oneOrMore(CharPredicate.Digit) ~ "." ~ oneOrMore(CharPredicate.Digit) }

  def requestUri = rule { SipUri | SipsUri }
  def requestLine = rule { method ~ SP ~ requestUri ~ SP ~ SipVersion ~ CRLF }
  def statusCode = rule { 3.times(CharPredicate.Digit) }
  def reasonPhrase = rule { zeroOrMore(reserved | unreserved | escaped | UTF8NONASCII | UTF8CONT | WSP) }
  def statusLine = rule { SipVersion ~ SP ~ statusCode ~ reasonPhrase ~ CRLF }
  
  def headerValue = rule { zeroOrMore(TEXTUTF8char | UTF8CONT | LWS) }
  def extensionHeader = rule { capture(token) ~ HCOLON ~ capture(headerValue) ~> ((n,v) => GenericHeader(n, v)) }

  def messageHeader = rule { (callIdHeader | extensionHeader) ~ CRLF }
  def messageBody = rule { zeroOrMore(CharPredicate.All) }

  def request = rule { requestLine ~ zeroOrMore(messageHeader) ~ CRLF ~ optional(messageBody) }
  def response = rule { statusLine ~ zeroOrMore(messageHeader) ~ CRLF ~ optional(messageBody) }
  def SipMessage = rule { request | response }


  /* Handled headers */
  def callIdHeader = rule { ("Call-Id" | "i") ~ HCOLON ~ capture(word ~ optional('@' ~ word)) ~> (i => CallId(i)) }


}


