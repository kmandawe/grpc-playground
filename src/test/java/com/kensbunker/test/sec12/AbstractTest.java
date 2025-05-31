package com.kensbunker.test.sec12;

import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec12.BankServiceGrpc;
import com.kensbunker.sec12.BankService;
import com.kensbunker.test.common.AbstractChannelTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTest extends AbstractChannelTest {

  private final GrpcServer grpcServer = GrpcServer.create(new BankService());
  protected BankServiceGrpc.BankServiceStub bankStub;
  protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;

  @BeforeAll
  public void setup() {
    this.grpcServer.start();
    this.bankStub = BankServiceGrpc.newStub(channel);
    this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
  }

  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }
}
