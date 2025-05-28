package com.kensbunker.common;

import com.kensbunker.sec06.BankService;
import com.kensbunker.sec06.TransferService;

public class Demo {
  public static void main(String[] args) {
    GrpcServer.create(new BankService(), new TransferService()).start().await();
  }
}
