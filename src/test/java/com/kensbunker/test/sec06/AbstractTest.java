package com.kensbunker.test.sec06;

import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec06.BankServiceGrpc;
import com.kensbunker.sec06.BankService;
import com.kensbunker.test.common.AbstractChannelTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTest extends AbstractChannelTest {
  private final GrpcServer grpcServer = GrpcServer.create(new BankService());
  protected BankServiceGrpc.BankServiceStub asyncStub;
  protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

  @BeforeAll
  public void setup() {
    this.grpcServer.start();
    this.asyncStub = BankServiceGrpc.newStub(channel);
    this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
  }

  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }
}
