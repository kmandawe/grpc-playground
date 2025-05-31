package com.kensbunker.test.sec12;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kensbunker.models.sec12.AccountBalance;
import com.kensbunker.models.sec12.BalanceCheckRequest;
import com.kensbunker.test.common.ResponseObserver;
import com.kensbunker.test.sec12.interceptors.DeadlineInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class Lec03DeadlineTest extends AbstractInterceptorTest {

  @Override
  protected List<ClientInterceptor> getClientInterceptors() {
    return List.of(new DeadlineInterceptor(Duration.ofSeconds(2)));
  }

  @Test
  public void blockingDeadlineTest() {

    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
    var response = this.bankBlockingStub.getAccountBalance(request);
  }

  @Test
  public void asyncDeadlineTest() {

    var observer = ResponseObserver.<AccountBalance>create();
    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
    this.bankStub.getAccountBalance(request, observer);
    observer.await();
    assertTrue(observer.getItems().isEmpty());
    assertEquals(Code.DEADLINE_EXCEEDED, Status.fromThrowable(observer.getThrowable()).getCode());
  }
}
