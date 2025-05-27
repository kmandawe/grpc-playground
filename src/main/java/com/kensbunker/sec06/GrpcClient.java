package com.kensbunker.sec06;

import com.kensbunker.models.sec06.BalanceCheckRequest;
import com.kensbunker.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcClient {
  private static final Logger LOG = LoggerFactory.getLogger(GrpcClient.class);

  public static void main(String[] args) {
    var channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
    var stub = BankServiceGrpc.newBlockingStub(channel);
    var balance =
        stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build());
    LOG.info("balance: {}", balance);
  }
}
