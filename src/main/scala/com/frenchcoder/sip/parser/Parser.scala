package com.frenchcoder.sip.parser

import org.parboiled2._

object MessageParser {
  val reserved = CharPredicate(";/?:@&=+$,")
  val mark = CharPredicate("-_.!~*'()")
  val unreserved = CharPredicate.AlphaNum ++ mark
  val userUnreserved  =  CharPredicate("&=+$,;?/")
  val hnvUnreserved  =  CharPredicate("[]/?:+$")
  val SP = CharPredicate(' ')
  val WSP = CharPredicate(" \t")
}

class MessageParser(val input: ParserInput) extends Parser with TelParser with IpAddressParser {
  import MessageParser._

  def CRLF = rule { "\r\n" }
  def LWS = rule { optional(zeroOrMore(WSP) ~ CRLF) ~ oneOrMore(WSP) }
  def SWS = rule { optional(LWS) }
  def HCOLON = rule { zeroOrMore(WSP) ~ ":" ~ SWS }

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

// Accept         =  "Accept" HCOLON
//                    [ accept-range *(COMMA accept-range) ]
// accept-range   =  media-range *(SEMI accept-param)
// media-range    =  ( "*/*"
//                   / ( m-type SLASH "*" )
//                   / ( m-type SLASH m-subtype )
//                   ) *( SEMI m-parameter )
// accept-param   =  ("q" EQUAL qvalue) / generic-param
// qvalue         =  ( "0" [ "." 0*3DIGIT ] )
//                   / ( "1" [ "." 0*3("0") ] )
// generic-param  =  token [ EQUAL gen-value ]
// gen-value      =  token / host / quoted-string

  

  // Accept
  // Accept-Encoding
  // Accept-Language
  // Alert-Info
  // Allow
  // Authentication-Info
  // Authorization
  // Call-ID
  // Call-Info
  // Contact
  // Content-Disposition
  // Content-Encoding
  // Content-Language
  // Content-Length
  // Content-Type
  // CSeq
  // Date
  // Error-Info
  // Expires
  // From
  // In-Reply-To
  // Max-Forwards
  // MIME-Version
  // Min-Expires
  // Organization
  // Priority
  // Proxy-Authenticate
  // Proxy-Authorization
  // Proxy-Require
  // Record-Route
  // Reply-To
  // Require
  // Retry-After
  // Route
  // Server
  // Subject
  // Supported
  // Timestamp
  // To
  // Unsupported
  // User-Agent
  // Via
  // Warning
  // WWW-Authenticate
  // extension-header
}


