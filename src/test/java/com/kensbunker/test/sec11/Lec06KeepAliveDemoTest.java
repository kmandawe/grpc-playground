package com.kensbunker.test.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec11.BalanceCheckRequest;
import com.kensbunker.models.sec11.BankServiceGrpc;
import com.kensbunker.sec11.DeadlineBankService;
import com.kensbunker.test.common.AbstractChannelTest;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec06KeepAliveDemoTest extends AbstractChannelTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec06KeepAliveDemoTest.class);

  private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
  private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;

  @BeforeAll
  public void setup() {
    this.grpcServer.start();
    this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
  }

  @Test
  public void keepAliveDemo() {
    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
    var response = this.bankBlockingStub.getAccountBalance(request);
    LOG.info("{}", response);

    // just blocking the thread for 30 seconds
    Uninterruptibles.sleepUninterruptibly(30, TimeUnit.SECONDS);
  }

  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }
}
