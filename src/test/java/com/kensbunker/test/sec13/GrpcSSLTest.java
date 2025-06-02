package com.kensbunker.test.sec13;

import com.kensbunker.models.sec13.BalanceCheckRequest;
import com.kensbunker.models.sec13.BankServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcSSLTest extends AbstractTest {
  private static final Logger LOG = LoggerFactory.getLogger(GrpcSSLTest.class);

  @Test
  public void clientWithSSLTest() {
    var channel = NettyChannelBuilder.forAddress("localhost", 6565)
        .sslContext(clientSslContext())
        .build();

    var stub = BankServiceGrpc.newBlockingStub(channel);

    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();

    var response = stub.getAccountBalance(request);

    LOG.info("{}", response);

    channel.shutdownNow();
  }
}
