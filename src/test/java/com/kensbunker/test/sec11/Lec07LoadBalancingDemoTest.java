package com.kensbunker.test.sec11;

import com.kensbunker.models.sec06.BalanceCheckRequest;
import com.kensbunker.models.sec06.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec07LoadBalancingDemoTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec07LoadBalancingDemoTest.class);
  private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
  private ManagedChannel channel;

  @BeforeAll
  public void setup() {
    this.channel = ManagedChannelBuilder.forAddress("localhost", 8585).usePlaintext().build();
    this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
  }

  @Test
  public void loadBalancingDemo() {
    for(int i = 1; i <= 10; i++) {
      var request = BalanceCheckRequest.newBuilder().setAccountNumber(i).build();
      var response = this.bankBlockingStub.getAccountBalance(request);
      LOG.info("{}", response);
    }
  }

  @AfterAll
  public void stop() {
    this.channel.shutdown();
  }
}
