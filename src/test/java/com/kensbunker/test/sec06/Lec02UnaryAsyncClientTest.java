package com.kensbunker.test.sec06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.protobuf.Empty;
import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.BalanceCheckRequest;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import javax.xml.transform.sax.SAXResult;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02UnaryAsyncClientTest extends AbstractTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec02UnaryAsyncClientTest.class);

  @Test
  public void getBalanceTest() throws InterruptedException {
    var request = BalanceCheckRequest.newBuilder()
        .setAccountNumber(1)
        .build();
    var latch = new CountDownLatch(1);
    this.asyncStub.getAccountBalance(request, new StreamObserver<AccountBalance>() {
      @Override
      public void onNext(AccountBalance accountBalance) {
        LOG.info("async balance received: {}", accountBalance);
        try {
          assertEquals(99, accountBalance.getBalance());
        } finally{
          latch.countDown();
        }

      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onCompleted() {

      }
    });
    latch.await();
  }

  @Test
  public void getAllAccountsTest() {
    var accounts = this.blockingStub.getAllAccounts(Empty.getDefaultInstance());
    accounts.getAccountsList().forEach(account -> LOG.info("account: {}", account));
    assertEquals(10, accounts.getAccountsCount());
  }
}
