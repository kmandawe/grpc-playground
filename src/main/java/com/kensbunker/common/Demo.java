package com.kensbunker.common;

import com.kensbunker.sec06.BankService;
import com.kensbunker.sec06.TransferService;
import com.kensbunker.sec07.FlowControlService;

public class Demo {
  public static void main(String[] args) {
    GrpcServer.create(new FlowControlService()).start().await();
  }
}
