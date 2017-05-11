package com.frenchcoder.sip

case class Status(status: Int)

class Response[A](val status: Status, headers: Headers, body: A) extends Message[A](headers, body) {
  
}

