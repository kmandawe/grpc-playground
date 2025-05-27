package com.kensbunker.sec06;

import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.BalanceCheckRequest;
import com.kensbunker.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcClient {
  private static final Logger LOG = LoggerFactory.getLogger(GrpcClient.class);

  public static void main(String[] args) throws InterruptedException {
    var channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
    var stub = BankServiceGrpc.newFutureStub(channel);
    var future = stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build());

    try {
      var balance = future.get();
      LOG.info("balance: {}", balance);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }

    LOG.info("done");
    Thread.sleep(Duration.ofSeconds(1));
  }
}
