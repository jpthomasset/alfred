package com.frenchcoder.sip


case class Method(name: String)

class Request[A](val method: Method, headers: Headers, body: A) extends Message[A](headers, body) {
  
}


