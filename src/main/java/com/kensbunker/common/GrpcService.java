package com.kensbunker.common;

import com.kensbunker.sec06.BankService;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class GrpcService {
  public static void main(String[] args) throws IOException, InterruptedException {
    var server = ServerBuilder.forPort(6565).addService(new BankService()).build();
    server.start();
    server.awaitTermination();
  }
}
