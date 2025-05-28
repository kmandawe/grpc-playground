package com.kensbunker.test.sec06;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.protobuf.Empty;
import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.AllAccountsResponse;
import com.kensbunker.models.sec06.BalanceCheckRequest;
import com.kensbunker.test.common.ResponseObserver;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02UnaryAsyncClientTest extends AbstractTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec02UnaryAsyncClientTest.class);

  @Test
  public void getBalanceTest() throws InterruptedException {
    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();

    var observer = ResponseObserver.<AccountBalance>create();
    this.asyncStub.getAccountBalance(request, observer);
    observer.await();
    assertEquals(1, observer.getItems().size());
    assertEquals(100, observer.getItems().getFirst().getBalance());
    assertNull(observer.getThrowable());
  }

  @Test
  public void getAllAccountsTest() {
    var observer = ResponseObserver.<AllAccountsResponse>create();
    this.asyncStub.getAllAccounts(Empty.getDefaultInstance(), observer);
    observer.await();
    assertEquals(1, observer.getItems().size());
    assertEquals(10, observer.getItems().getFirst().getAccountsCount());
    assertNull(observer.getThrowable());
  }
}
