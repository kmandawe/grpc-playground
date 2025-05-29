package com.kensbunker.test.sec11;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kensbunker.models.sec11.AccountBalance;
import com.kensbunker.models.sec11.BalanceCheckRequest;
import com.kensbunker.test.common.ResponseObserver;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01UnaryDeadlineTest extends AbstractTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec01UnaryDeadlineTest.class);

  @Test
  public void blockingDeadlineTest() {

    var ex =
        assertThrows(
            StatusRuntimeException.class,
            () -> {
              var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
              var response =
                  this.bankBlockingStub
                      .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                      .getAccountBalance(request);
            });
    assertEquals(Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());
  }

  @Test
  public void asyncDeadlineTest() {

    var observer = ResponseObserver.<AccountBalance>create();
    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
    this.bankStub
        .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
        .getAccountBalance(request, observer);
    observer.await();
    assertTrue(observer.getItems().isEmpty());
    assertEquals(Code.DEADLINE_EXCEEDED, Status.fromThrowable(observer.getThrowable()).getCode());
  }
}
