package com.frenchcoder.sip

abstract class Message[A](val headers: Headers, val body: A)

