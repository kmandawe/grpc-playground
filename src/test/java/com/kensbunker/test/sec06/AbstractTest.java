package com.kensbunker.test.sec06;

import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec06.BankServiceGrpc;
import com.kensbunker.models.sec06.TransferServiceGrpc;
import com.kensbunker.sec06.BankService;
import com.kensbunker.sec06.TransferService;
import com.kensbunker.test.common.AbstractChannelTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTest extends AbstractChannelTest {
  private final GrpcServer grpcServer = GrpcServer.create(new BankService(), new TransferService());
  protected BankServiceGrpc.BankServiceStub bankStub;
  protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
  protected TransferServiceGrpc.TransferServiceStub transferStub;

  @BeforeAll
  public void setup() {
    this.grpcServer.start();
    this.bankStub = BankServiceGrpc.newStub(channel);
    this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    this.transferStub = TransferServiceGrpc.newStub(channel);
  }

  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }
}
