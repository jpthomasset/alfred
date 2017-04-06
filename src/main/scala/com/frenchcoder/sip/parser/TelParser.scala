package com.frenchcoder.sip.parser

import org.parboiled2._

object TelParser {
  val visualSeparator = CharPredicate("-.()")
  val phoneDigit = CharPredicate.Digit ++ visualSeparator
  val dtmfDigit = CharPredicate("*#ABCD")
  val oneSecondPause = CharPredicate('p')
  val waitForDialTone = CharPredicate('w')
  val pauseCharacter = oneSecondPause ++ waitForDialTone
  val tokenChar = {
    CharPredicate('\u0021') ++
    CharPredicate('\u0023' to '\u0027') ++
    CharPredicate('\u002A' to '\u002B') ++
    CharPredicate('\u002D' to '\u002E') ++
    CharPredicate('\u0030' to '\u0039') ++
    CharPredicate('\u0041' to '\u005A') ++
    CharPredicate('\u005E' to '\u007A') ++
    CharPredicate('\u007C') ++
    CharPredicate('\u007E')
  }
}

trait TelParser { this: Parser =>
  import TelParser._

  def isdnSubaddress = rule { ";isub="  ~ oneOrMore(phoneDigit) }
  def postDial = rule { ";postd=" ~ oneOrMore(phoneDigit | dtmfDigit | pauseCharacter) }
  def localNetworkPrefix  = rule { oneOrMore(phoneDigit | dtmfDigit | pauseCharacter) }
  def globalNetworkPrefix = rule { "+" ~ oneOrMore(phoneDigit) }
  def networkPrefix = rule { globalNetworkPrefix | localNetworkPrefix }
  def privatePrefix = rule {
    (CharPredicate('\u0021' to '\u0022') |
      CharPredicate('\u0024' to '\u0027') |
      CharPredicate('\u002C') |
      CharPredicate('\u002F') |
      CharPredicate('\u003A') |
      CharPredicate('\u003C' to '\u0040') |
      CharPredicate('\u0045' to '\u004F') |
      CharPredicate('\u0051' to '\u0056') |
      CharPredicate('\u0058' to '\u0060') |
      CharPredicate('\u0065' to '\u006F') |
      CharPredicate('\u0071' to '\u0076') |
      CharPredicate('\u0078' to '\u007E')
    ) ~ zeroOrMore(CharPredicate('\u0021' to '\u003A') | CharPredicate('\u003C' to '\u007E'))
  }

  def phoneContextIdent = rule { networkPrefix | privatePrefix }
  def areaSpecifier = rule { ";phone-context=" ~ phoneContextIdent }
  def domainLabel = rule { CharPredicate.Alpha ~ zeroOrMore(zeroOrMore(CharPredicate.AlphaNum ++ CharPredicate('-')) ~ CharPredicate.Alpha) }
  def domain = rule { domainLabel + zeroOrMore('.' ~ domainLabel) }
  def serviceProvider = rule { ";tsp=" ~ domain }
  def quotedString = rule { '"' ~ zeroOrMore( ('\\' ~ CharPredicate.All) |  (CharPredicate('\u0020' to '\u0021') | CharPredicate('\u0023' to '\u007E') | CharPredicate('\u0080' to '\u00FF'))) ~ '"' }
  def futureExtension = rule { ";" ~ oneOrMore(tokenChar) ~ optional("=" ~ (oneOrMore(tokenChar) ~ optional('?' ~ oneOrMore(tokenChar)) | quotedString)) }
  def basePhoneNumber = rule { oneOrMore(phoneDigit) }
  def localPhoneNumber = rule {
    oneOrMore(phoneDigit | dtmfDigit | pauseCharacter) ~
    optional(isdnSubaddress) ~
    optional(postDial) ~
    areaSpecifier ~
    zeroOrMore(areaSpecifier | serviceProvider | futureExtension)
  }
  def globalPhoneNumber = rule {
    "+" ~ basePhoneNumber ~ optional(isdnSubaddress) ~ optional(postDial) ~ zeroOrMore(areaSpecifier | serviceProvider | futureExtension) 
  }

  def telephoneSubscriber = rule { globalPhoneNumber | localPhoneNumber }
} 
