package com.kensbunker.test.sec12;

import com.kensbunker.models.sec12.Money;
import com.kensbunker.models.sec12.WithdrawRequest;
import com.kensbunker.test.common.ResponseObserver;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02ExecutorCallOptionsTest extends AbstractTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec02ExecutorCallOptionsTest.class);

  @Test
  public void executorDemo() {
    var observer = ResponseObserver.<Money>create();
    var request = WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(30).build();
    this.bankStub
        .withExecutor(Executors.newVirtualThreadPerTaskExecutor())
        .withdraw(request, observer);

    observer.await();
  }
}
