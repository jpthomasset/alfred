package com.frenchcoder.sip

trait AbstractContent

case object EmptyContent extends AbstractContent

case class Content(contentType: String, payload: String) extends AbstractContent
