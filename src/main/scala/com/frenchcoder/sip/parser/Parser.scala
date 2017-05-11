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
  def HCOLON = rule { zeroOrMore(WSP) ~ ':' ~ SWS }
  def RAQUOT = rule { '>' ~ SWS }
  def LAQUOT = rule { SWS ~ '<' }
  def SEMI = rule { SWS ~ ';' ~ SWS }
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
  def quotedString  =  rule { SWS ~ '"' ~ zeroOrMore(qdtext | quotedPair ) ~ '"' }

  def word = rule { oneOrMore(wordChar) }

  def escaped = rule { '%' ~ CharPredicate.HexDigit ~ CharPredicate.HexDigit }
  def userInfo: Rule1[UserInfo] = rule { capture( user | telephoneSubscriber ) ~ optional(":" ~ capture(password)) ~ "@" ~> ((u, p) => UserInfo(u, p)) }
  def user = rule { oneOrMore(unreserved | escaped | userUnreserved ) }
  def password = rule { oneOrMore(unreserved | escaped | CharPredicate("&=+$,")) }
  def method = rule { "REGISTER" | "INVITE" | "ACK" | "CANCEL" | "BYE" | "OPTIONS" }
  def port = rule { oneOrMore(CharPredicate.Digit) }
  def host = rule { domain | IpV4Address | IpV6Address }
  def hostPort = rule { capture(host) ~ optional(':' ~ capture(port)) ~> ((h, p) => HostAndPort(h, p.map(_.toInt))) }
  def token = rule { oneOrMore(CharPredicate.AlphaNum | CharPredicate("-.!%*_+`'~")) }
  def transportParam = rule { "transport=" ~ capture("udp" | "tcp" | "sctp" | "tls" | token) ~> (v => UriParameter("transport", Some(v))) }
  def userParam = rule { "user=" ~ capture( "phone" | "ip" | token) ~> (v => UriParameter("user", Some(v)))}
  def methodParam = rule { "method=" ~ capture(method) ~> (v => UriParameter("method", Some(v))) }
  def ttl = rule { (1 to 3).times(CharPredicate.Digit) }
  def ttlParam = rule { "ttl=" ~ capture(ttl) ~> (v => UriParameter("ttl", Some(v)))}
  def maddrParam  = rule { "maddr=" ~ capture(host) ~> (v => UriParameter("maddr", Some(v))) }
  def paramNameValue = rule { oneOrMore(CharPredicate("[]/:&+$") | unreserved | escaped) }
  def lrParam = rule { "lr" ~ push(UriParameter("lr", None)) }
  def otherParam = rule { capture(paramNameValue) ~ optional('=' ~ capture(paramNameValue)) ~> ((n,v) => UriParameter(n, v))}
  def hname = rule { oneOrMore(hnvUnreserved | unreserved | escaped) }
  def hvalue = rule { zeroOrMore(hnvUnreserved | unreserved | escaped) }
  def header = rule { capture(hname) ~ "=" ~ capture(hvalue) ~> ((n,v) => UriHeader(n, v)) }
  def headers: Rule1[Seq[UriHeader]] = rule { "?" ~ capture(header) ~ zeroOrMore("&" ~ header) ~> ((a:UriHeader, b: Seq[UriHeader]) => a +: b)}
  def uriParameter = rule { transportParam | userParam | methodParam | ttlParam | maddrParam | lrParam | otherParam }
  def uriParameters = rule { zeroOrMore(';' ~ uriParameter) }

  def sipUri = rule { "sip:" ~ optional(userInfo) ~ hostPort ~ uriParameters ~ optional(headers) ~> ((u, h, p, hdr) => SipUri(false, u, h, p, hdr))}
  def sipsUri = rule { "sips:" ~ optional(userInfo) ~ hostPort ~ uriParameters ~ optional(headers)}
  def sipVersion = rule { "SIP/" ~ oneOrMore(CharPredicate.Digit) ~ "." ~ oneOrMore(CharPredicate.Digit) }

  def addrSpec =  rule { sipUri | sipsUri | absoluteURI }
  def absoluteURI =  rule { scheme ~ ":" ~ ( hierPart | opaquePart ) }
  def hierPart =  rule { (netPath | absPath ) ~ optional("?" ~ query) }
  def netPath =  rule { "//" ~ authority ~ optional(absPath) }
  def absPath =  rule { "/" ~ pathSegments }

  def opaquePart    =  rule { uricNoSlash ~ zeroOrMore(uric) }
  def uric = rule { reserved | unreserved | escaped }
  def uricNoSlash = rule { unreserved | escaped | CharPredicate(";?:@&=+$,") }

  def pathSegments  =  rule { segment ~ zeroOrMore("/" ~ segment) }
  def segment = rule { zeroOrMore(pchar) ~ zeroOrMore(";" ~ param) }
  def param  = rule { zeroOrMore(pchar) }

  def pchar = rule { unreserved | escaped | CharPredicate(":@&=+$,") }
  def scheme =  rule { CharPredicate.Alpha ~ zeroOrMore(CharPredicate.AlphaNum | CharPredicate("+-.")) }
  def authority = rule { srvr | regName }
  def srvrNonEmpty = rule { optional(userInfo ~ "@") ~ hostPort ~> ((u, h) => Server(u, h)) }
  def srvr =  rule { optional(srvrNonEmpty) ~> (s => s.getOrElse(EmptyServer)) }
  def regName  =  rule { capture(oneOrMore(unreserved | escaped | CharPredicate("$,;:@&=+"))) ~> (s => RegName(s)) }
  def query =  rule { zeroOrMore(uric) }

  def tagParam = rule { "tag" ~ EQUAL ~ token }
  def genericParam  = rule { token ~ optional(EQUAL ~ genValue) }
  def genValue = rule { token | host | quotedString }

  def nameAddr =  rule { optional(displayName) ~ LAQUOT ~ addrSpec ~ RAQUOT }
  def displayName = rule { zeroOrMore(token ~ LWS) | quotedString }

  def fromParam = rule { tagParam | genericParam }
  def toParam = fromParam

  def requestUri = rule { sipUri | sipsUri | absoluteURI }
  def requestLine = rule { method ~ SP ~ requestUri ~ SP ~ sipVersion ~ CRLF }
  def statusCode = rule { 3.times(CharPredicate.Digit) }
  def reasonPhrase = rule { zeroOrMore(reserved | unreserved | escaped | UTF8NONASCII | UTF8CONT | WSP) }
  def statusLine = rule { sipVersion ~ SP ~ statusCode ~ reasonPhrase ~ CRLF }
  
  def headerValue = rule { zeroOrMore(TEXTUTF8char | UTF8CONT | LWS) }
  def extensionHeader = rule { capture(token) ~ HCOLON ~ capture(headerValue) ~> ((n,v) => GenericHeader(n, v)) }

  def messageHeader = rule { (callIdHeader | extensionHeader) ~ CRLF }
  def messageBody = rule { zeroOrMore(CharPredicate.All) }

  def request = rule { requestLine ~ zeroOrMore(messageHeader) ~ CRLF ~ optional(messageBody) }
  def response = rule { statusLine ~ zeroOrMore(messageHeader) ~ CRLF ~ optional(messageBody) }
  def sipMessage = rule { request | response }


  /* Handled headers */
  def callIdHeader = rule { ("Call-Id" | "i") ~ HCOLON ~ capture(word ~ optional('@' ~ word)) ~> (i => CallId(i)) }
  def cSeqHeader = rule { ("CSeq") ~ HCOLON ~ capture(oneOrMore(CharPredicate.Digit)) ~> (i => CSeq(i.toLong)) }
  def fromHeader = rule { ("From" | "f") ~ HCOLON ~ (nameAddr | addrSpec) ~ zeroOrMore(SEMI ~ fromParam) }


}


