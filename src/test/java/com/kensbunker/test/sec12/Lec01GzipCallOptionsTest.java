package com.kensbunker.test.sec12;

import com.kensbunker.models.sec12.BalanceCheckRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01GzipCallOptionsTest extends AbstractTest{
  private static final Logger LOG = LoggerFactory.getLogger(Lec01GzipCallOptionsTest.class);

  @Test
  public void gzipDemo() {
    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
    var response = this.bankBlockingStub.withCompression("gzip").getAccountBalance(request);
    LOG.info("{}", response);
  }
}
