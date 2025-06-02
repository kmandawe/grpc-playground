package com.kensbunker.test.sec12;

import com.kensbunker.models.sec12.BalanceCheckRequest;
import com.kensbunker.test.sec12.interceptors.DeadlineInterceptor;
import com.kensbunker.test.sec12.interceptors.GzipRequestInterceptor;
import io.grpc.ClientInterceptor;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

public class Lec04GzipInterceptorTest extends AbstractInterceptorTest {

  @Override
  protected List<ClientInterceptor> getClientInterceptors() {
    return List.of(new GzipRequestInterceptor(), new DeadlineInterceptor(Duration.ofSeconds(2)));
  }

  @Test
  public void gzipDemo() {
    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
    var response = this.bankBlockingStub.getAccountBalance(request);
  }
}
