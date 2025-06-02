package com.kensbunker.common;

import com.kensbunker.sec12.BankService;
import com.kensbunker.sec12.interceptors.ApiKeyValidationInterceptor;

public class Demo {
  private static class BankInstance1 {
    public static void main(String[] args) {
      GrpcServer.create(6565, new BankService()).start().await();
    }
  }

  private static class BankInstance2 {
    public static void main(String[] args) {
      GrpcServer.create(
              6565,
              builder -> {
                builder
                    .addService(new BankService())
                    .intercept(new ApiKeyValidationInterceptor());
              })
          .start()
          .await();
    }
  }
}
