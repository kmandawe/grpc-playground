package com.kensbunker.test.sec06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.protobuf.Empty;
import com.kensbunker.models.sec06.BalanceCheckRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01UnaryBlockingClientTest extends AbstractTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec01UnaryBlockingClientTest.class);

  @Test
  public void getBalanceTest() {
    var request = BalanceCheckRequest.newBuilder()
        .setAccountNumber(1)
        .build();
    var balance = this.bankBlockingStub.getAccountBalance(request);
    LOG.info("unary balance received: {}", balance);
    assertEquals(100, balance.getBalance());
  }

  @Test
  public void getAllAccountsTest() {
    var accounts = this.bankBlockingStub.getAllAccounts(Empty.getDefaultInstance());
    accounts.getAccountsList().forEach(account -> LOG.info("account: {}", account));
    assertEquals(10, accounts.getAccountsCount());
  }
}
