package com.frenchcoder.alfred.sip

class SequenceGenerator {
  private var seq = 0l
  def next : Long = {
    seq = seq + 1
    seq
  }
}
