package com.kensbunker.test.sec11;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.util.concurrent.Uninterruptibles;
import com.kensbunker.models.sec11.Money;
import com.kensbunker.models.sec11.WithdrawRequest;
import com.kensbunker.test.common.ResponseObserver;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02ServerStreamingDeadlineTest extends AbstractTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec02ServerStreamingDeadlineTest.class);

  @Test
  public void blockingDeadlineTest() {
    var ex = assertThrows(StatusRuntimeException.class, () -> {
      var request = WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(50).build();
      var iterator = this.bankBlockingStub
          .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
          .withdraw(request);
      while (iterator.hasNext()) {
        iterator.next();
      }
    });
    assertEquals(Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());
  }

  @Test
  public void asyncDeadlineTest() {
    var observer = ResponseObserver.<Money>create();
    var request = WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(50).build();
    this.bankStub
        .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
        .withdraw(request, observer);
    observer.await();
    assertEquals(2, observer.getItems().size());
    assertEquals(Code.DEADLINE_EXCEEDED, Status.fromThrowable(observer.getThrowable()).getCode());
  }
}
