package com.kensbunker.test.sec06;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.kensbunker.models.sec06.Money;
import com.kensbunker.models.sec06.WithdrawRequest;
import com.kensbunker.test.common.ResponseObserver;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec03ServerStreamingClientTest extends AbstractTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec03ServerStreamingClientTest.class);

  @Test
  public void blockingClientWithdrawTest() {
    var request = WithdrawRequest.newBuilder().setAccountNumber(2).setAmount(20).build();
    var iterator = this.bankBlockingStub.withdraw(request);
    int count = 0;
    while (iterator.hasNext()) {
      var money = iterator.next();
      LOG.info("money received: {}", money);
      count++;
    }
    assertEquals(2, count);
  }

  @Test
  public void asyncClientWithdrawTest() {
    var request = WithdrawRequest.newBuilder().setAccountNumber(2).setAmount(20).build();
    var observer = ResponseObserver.<Money>create();
    this.bankStub.withdraw(request, observer);

    observer.await();
    assertEquals(2, observer.getItems().size());
    assertEquals(10, observer.getItems().getFirst().getAmount());
    assertNull(observer.getThrowable());
  }
}
