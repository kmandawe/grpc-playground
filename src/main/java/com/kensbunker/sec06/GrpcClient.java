package com.kensbunker.sec06;

import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.BalanceCheckRequest;
import com.kensbunker.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcClient {
  private static final Logger LOG = LoggerFactory.getLogger(GrpcClient.class);

  public static void main(String[] args) throws InterruptedException {
    var channel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();
    var stub = BankServiceGrpc.newStub(channel);
    stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build(), new StreamObserver<AccountBalance>() {
      @Override
      public void onNext(AccountBalance accountBalance) {
        LOG.info("accountBalance: {}", accountBalance);
      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onCompleted() {
        LOG.info("completed");
      }
    });

    LOG.info("done");
    Thread.sleep(Duration.ofSeconds(1));
  }
}
