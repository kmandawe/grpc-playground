package com.kensbunker.test.sec11;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.util.concurrent.Uninterruptibles;
import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec11.BankServiceGrpc;
import com.kensbunker.models.sec11.WithdrawRequest;
import com.kensbunker.sec11.DeadlineBankService;
import com.kensbunker.test.common.AbstractChannelTest;
import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec03WaitForReadyTest extends AbstractChannelTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec03WaitForReadyTest.class);

  private final GrpcServer grpcServer = GrpcServer.create(new DeadlineBankService());
  private BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;

  @BeforeAll
  public void setup() {
//    this.grpcServer.start();
    Runnable runnable = () -> {
      Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
      this.grpcServer.start();
    };
    Thread.ofVirtual().start(runnable);
    this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
  }

  @Test
  public void blockingDeadlineTest() {
    LOG.info("sending the request");
    var request = WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(50).build();
    var iterator = this.bankBlockingStub.withWaitForReady().withDeadline(Deadline.after(8, TimeUnit.SECONDS)).withdraw(request);
    while (iterator.hasNext()) {
      LOG.info("{}", iterator.next());
    }
  }

  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }
}
