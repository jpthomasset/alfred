package com.frenchcoder.sip.parser

import org.parboiled2._

class MessageParser(val input: ParserInput) extends Parser {

  def reserved = rule { anyOf(";/?:@&=+$,") }
  def mark = rule { anyOf("-_.!~*'()") }
  def unreserved = rule { CharPredicate.AlphaNum | mark } 
  def escaped = rule { '%' ~ CharPredicate.HexDigit ~ CharPredicate.HexDigit }
  def userUnreserved  =  rule { anyOf("&=+$,;?/") }
  def user = rule { oneOrMore(unreserved | escaped | userUnreserved ) }
  def password = rule { oneOrMore(unreserved | escaped | anyOf("&=+$,")) }

                    
  def method = rule { "REGISTER" | "INVITE" | "ACK" | "CANCEL" | "BYE" | "OPTIONS" }
  
}

