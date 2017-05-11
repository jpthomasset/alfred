package com.frenchcoder.sip

case class UserInfo(user: String, password: Option[String])
case class HostAndPort(host: String, port: Option[Int])
case class UriParameter(name: String, value: Option[String])
case class UriHeader(name: String, value: String)

sealed trait Authority
case object EmptyServer extends Authority
case class Server(userInfo: Option[UserInfo], hostAndPort: HostAndPort) extends Authority
case class RegName(name: String) extends Authority

sealed trait URI
case class SipUri(isSecured: Boolean, userInfo: Option[UserInfo], hostAndPort: HostAndPort, parameters: List[UriParameter] = List.empty, headers: List[UriHeader] = List.empty ) extends URI
case class TelUri(uri: String) extends URI

case class Address(uri: URI, displayName: Option[String] = None)
