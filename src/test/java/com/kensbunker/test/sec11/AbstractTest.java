package com.kensbunker.test.sec11;

import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec11.BankServiceGrpc;
import com.kensbunker.sec11.DeadlineBankService;
import com.kensbunker.test.common.AbstractChannelTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTest extends AbstractChannelTest {

  private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
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
