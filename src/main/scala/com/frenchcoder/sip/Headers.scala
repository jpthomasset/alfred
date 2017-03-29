package com.frenchcoder.sip

import javax.sip.{header => jsh}
import gov.nist.javax.sip.{header => gsh}

class Headers(val headers: Seq[(String, String)])

object Headers {
  def apply(headers: Iterator[jsh.Header]) : Headers = {
    new Headers(headers.map(h => headerToSeq(h.asInstanceOf[gsh.SIPHeader])).toSeq)
  }

  def headerToSeq(header: gsh.SIPHeader): (String, String) = {
    (header.getName, header.getValue)
  }

}
