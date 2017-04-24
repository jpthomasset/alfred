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
  val UTF8CONT = CharPredicate('\u0080' to '\u00BF')
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
  def EQUAL = rule { SWS ~ "=" ~ SWS }

  def quotedPair = rule { '\\' ~ (CharPredicate('\u0000' to '\u0009') | CharPredicate('\u000B' to '\u000C') | CharPredicate('\u000E' to '\u007F')) }
  def qdtext = rule { LWS | CharPredicate('\u0021') | CharPredicate('\u0023' to '\u005B') | CharPredicate('\u005D' to '\u007E') | UTF8NONASCII }


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

//  def mediaRange = rule { "*/*" | 
  def acceptParam = rule { ('q' ~ EQUAL ~ qvalue) | genericParam }
  def qvalue = rule {
    ('0' ~ optional('.' ~ optional((1 to 3).times(CharPredicate.Digit)))) |
    ('1' ~ optional('.' ~ optional((1 to 3).times('0'))))
  }
  def genericParam = rule { token ~ optional(EQUAL ~ genValue) }
  def genValue = rule { token | host | quotedString }

  

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


