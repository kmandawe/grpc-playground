package com.kensbunker.common;

import com.kensbunker.sec09.BankService;

public class Demo {
  public static void main(String[] args) {
    GrpcServer.create(new BankService()).start().await();
  }
}
